package com.devwiki.leafy.service.user;

import com.devwiki.leafy.dto.user.*;
import com.devwiki.leafy.exception.ResourceNotFoundException;
import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;
import com.devwiki.leafy.global.common.exception.type.ServerErrorException;
import com.devwiki.leafy.model.user.User;
import com.devwiki.leafy.repository.user.UserRepository;
import com.devwiki.leafy.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;
    /**
     * 모든 사용자 조회
     *
     * @return 모든 사용자 리스트
     */
    public List<UserResponseDto> getAllUsers() {
        List<User> userDtos = userRepository.findAll();
        return userDtos.stream().map(UserMapper::toResponseDto).collect(Collectors.toList());
    }

    /**
     * 사용자 ID로 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자
     */
    public UserResponseDto getUserResponseById(Long userId) {
        User user = findUserById(userId);
        return UserMapper.toResponseDto(user);
    }

    /**
     * 사용자 ID로 조회, 내부 로직 전용
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자
     */
    public UserDto getUserById(Long userId) {
        User user = findUserById(userId);
        return UserMapper.toDto(user);
    }


    /**
     * 새로운 사용자를 생성합니다.
     *
     * @param userRequestDto 새로 생성할 사용자 정보
     * @return 생성된 사용자 정보
     */
    public ResponseEntity<?> createUser(UserRequestDto userRequestDto) {

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ServerErrorException(BadStatusCode.EXISTING_EMAIL_EXCEPTION);
            // Or return a specific error response
        }
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword())); // 비밀번호 암호화
        log.info("Encoded password: " + userRequestDto.getPassword());

        // User user = new User(userRequestDto);
        User user = User.builder()
            .name(userRequestDto.getName())
            .email(userRequestDto.getEmail())
            .password(userRequestDto.getPassword())
            .gender(userRequestDto.getGender())
            .birthDate(userRequestDto.getBirthDate())
            .build();


        userRepository.save(user);

        String userId = String.valueOf(user.getUserId());
        String accessToken = jwtUtil.createAccessToken(Integer.parseInt(userId));
        String refreshToken = jwtUtil.createRefreshToken(Integer.parseInt(userId));


        // ★ 여기도 똑같이 Redis 저장 추가 ★
        redisTemplate.opsForValue().set(
            "RT:" + userId,
            refreshToken,
            jwtUtil.getRefreshTokenMaxAgeInSeconds(),
            TimeUnit.SECONDS
        );

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일과 비밀번호를 받아 사용자 정보를 조회합니다.
     *
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     * @return 조회된 사용자 정보
     */
    public UserResponseDto getUserByEmailAndPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // accessToken과 refreshToken 생성
                String userId = String.valueOf(user.getUserId());
                String accessToken = jwtUtil.createAccessToken(Integer.parseInt(userId));
                String refreshToken = jwtUtil.createRefreshToken(Integer.parseInt(userId));
                // Redis에 Refresh Token 저장
                redisTemplate.opsForValue().set(
                    "RT:" + userId,
                    refreshToken,
                    jwtUtil.getRefreshTokenMaxAgeInSeconds(),
                    TimeUnit.SECONDS
                );
                return UserMapper.userResponseDto(user, accessToken, refreshToken);
            }
        }
        return new UserResponseDto();
    }

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param userPutRequestDto 업데이트할 사용자 정보
     * @return 업데이트된 사용자 정보
     */
    public UserResponseDto updateUser(Long userId, UserPutRequestDto userPutRequestDto) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName(userPutRequestDto.getName());
        // 비밀번호 암호화
        if (userPutRequestDto.getPassword() != null) {
            userRequestDto.setPassword(passwordEncoder.encode(userPutRequestDto.getPassword()));
        }

        User user = findUserById(userId);
        userRequestDto.setEmail(user.getEmail());
        userRequestDto.setGender(user.getGender());

        user.updateEntity(userRequestDto);
        userRepository.save(user);

        return UserMapper.toResponseDto(user);
    }


    /**
     * 사용자 정보를 삭제합니다.
     *
     * @param userId 삭제할 사용자 Id
     */
    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
    }

    /**
     * 특정 id를 가진 식물 로그 조회
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("PlantLog", "id", userId));
    }

    public Map<String, String> reissueTokens(String refreshToken) {
        // 1. Refresh Token 검증
        jwtUtil.validateToken(refreshToken);

        // 2. Refresh Token에서 사용자 정보 추출
        Integer userId = Integer.valueOf(jwtUtil.getUserIdFromToken(refreshToken));

        // 3. Redis에서 저장된 Refresh Token과 비교
        String redisRefreshToken = redisTemplate.opsForValue().get("RT:" + userId);
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new ServerErrorException(BadStatusCode.INVALID_REFRESH_TOKEN);
        }

        // 4. 새로운 Access Token과 Refresh Token 생성
        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // 5. Redis에 새로운 Refresh Token 저장
        redisTemplate.opsForValue().set(
            "RT:" + userId,
            newRefreshToken,
            jwtUtil.getRefreshTokenMaxAgeInSeconds(),
            TimeUnit.SECONDS
        );

        // 6. 새로운 토큰 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        return tokens;
    }
}

package com.devwiki.leafy.dto.user;

import com.devwiki.leafy.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponseDto toResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setPassword(null);
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setBirthDate(user.getBirthDate());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        userResponseDto.setUpdatedAt(user.getUpdatedAt());
        return userResponseDto;
    }
    public static UserResponseDto userResponseDto(User user, String accessToken, String refreshToken) {
        UserResponseDto userResponseDto = toResponseDto(user);
        userResponseDto.setAccessToken(accessToken);
        userResponseDto.setRefreshToken(refreshToken);
        return userResponseDto;

    }

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setGender(user.getGender());
        userDto.setBirthDate(user.getBirthDate());
        return userDto;
    }
}

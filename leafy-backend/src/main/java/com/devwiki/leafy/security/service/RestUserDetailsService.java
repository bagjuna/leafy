package com.devwiki.leafy.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devwiki.leafy.dto.user.UserContext;
import com.devwiki.leafy.dto.user.UserDto;
import com.devwiki.leafy.model.user.Role;
import com.devwiki.leafy.model.user.User;
import com.devwiki.leafy.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service("userDetailsService")
@RequiredArgsConstructor
public class RestUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username).orElseThrow(

		);

		List<GrantedAuthority> authorities = user.getUserRoles()
			.stream()
			.map(Role::getRoleName)
			.collect(Collectors.toSet())
			.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		ModelMapper mapper = new ModelMapper();
		UserDto loginDto = mapper.map(user, UserDto.class);

		return new UserContext(loginDto, authorities);

	}
}

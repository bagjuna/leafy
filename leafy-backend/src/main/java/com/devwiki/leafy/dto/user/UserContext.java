package com.devwiki.leafy.dto.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class UserContext implements UserDetails {

	private final UserDto loginDto;
	private final List<GrantedAuthority> roles;

	public UserContext(UserDto loginDto, List<GrantedAuthority> roles) {
		this.loginDto = loginDto;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return loginDto.getPassword();
	}

	@Override
	public String getUsername() {
		return loginDto.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

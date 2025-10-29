package com.devwiki.leafy.model.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.devwiki.leafy.dto.user.UserDto;
import com.devwiki.leafy.dto.user.UserRequestDto;
import com.devwiki.leafy.dto.user.UserResponseDto;
import com.devwiki.leafy.util.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender")
    private String gender;

    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.MERGE})
    @JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
        @JoinColumn(name = "role_id") })
    @ToString.Exclude
    private Set<Role> userRoles = new HashSet<>();

    @Column(name = "birthDate")
    private LocalDate birthDate;


    @Builder
    public User(String email, String name, String password, String gender, LocalDate birthDate) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public void updateEntity(UserRequestDto userRequestDto) {
        this.name = userRequestDto.getName();
        this.password = userRequestDto.getPassword();
        this.gender = userRequestDto.getGender();
        this.birthDate = userRequestDto.getBirthDate();
    }

    public User(UserDto userDto) {
        this.userId = userDto.getUserId();
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.gender = userDto.getGender();
        this.birthDate = userDto.getBirthDate();
    }

    public User(UserResponseDto userResponseDto) {
        this.userId = userResponseDto.getUserId();
        this.name = userResponseDto.getName();
        this.email = userResponseDto.getEmail();
        this.password = userResponseDto.getPassword();
        this.gender = userResponseDto.getGender();
        this.birthDate = userResponseDto.getBirthDate();
    }

    public User(UserRequestDto createDto) {
        this.name = createDto.getName();
        this.email = createDto.getEmail();
        this.password = createDto.getPassword();
        this.gender = createDto.getGender();
        this.birthDate = createDto.getBirthDate();
    }
}

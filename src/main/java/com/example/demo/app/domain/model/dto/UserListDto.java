package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class UserListDto {
    private List<UserDto> UserDto;

    @Builder
    public UserListDto(List<UserDto> userDto) {
        this.UserDto = UserDto;
    }
}

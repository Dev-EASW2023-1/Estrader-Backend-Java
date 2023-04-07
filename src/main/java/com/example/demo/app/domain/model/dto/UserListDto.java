package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserListDto {

    @NotBlank
    private List<UserDto> userDto;

    @Builder
    public UserListDto(List<UserDto> userDto) {
        this.userDto = userDto;
    }
}

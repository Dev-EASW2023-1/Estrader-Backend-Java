package kr.easw.estrader.app.domain.model.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserListDto {
    private List<UserDto> userDto;

    @Builder
    public UserListDto(List<UserDto> userDto) {
        this.userDto = userDto;
    }
}

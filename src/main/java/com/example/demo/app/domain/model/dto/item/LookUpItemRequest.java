package com.example.demo.app.domain.model.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LookUpItemRequest {
    @NotBlank
    private String photo;

    @Builder
    public LookUpItemRequest(String photo){
        this.photo = photo;
    }
}

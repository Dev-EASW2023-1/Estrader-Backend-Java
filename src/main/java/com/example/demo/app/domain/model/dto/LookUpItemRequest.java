package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LookUpItemRequest {
    @NotBlank
    private String picture;

    @Builder
    public LookUpItemRequest(String picture){
        this.picture = picture;
    }
}

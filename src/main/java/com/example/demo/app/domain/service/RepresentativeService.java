package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.*;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.model.entity.RepresentativeEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.RepresentativeRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepresentativeService {
    private final RepresentativeRepository representativeRepository;
    private final UserRepository userRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ItemRepository itemRepository;

    @Transactional
    public void addRepresentativeInfo(
            String username,
            String password,
            String fcmToken,
            String corporateRegistrationNumber
    ) {
        RepresentativeEntity representativeInfo = RepresentativeEntity.builder()
                .username(username)
                .password(password)
                .fcmToken(fcmToken)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .build();
        representativeRepository.save(representativeInfo);
    }

    public RepresentativeListDto findRepresentativeInfo() {
        List<RepresentativeEntity> listRepresentativeInfo = representativeRepository.findAll();

        List<RepresentativeDto> listRepresentativeDto = listRepresentativeInfo.stream()
                .map(m -> new RepresentativeDto(
                        m.getUsername(),
                        m.getPassword(),
                        m.getFcmToken(),
                        m.getCorporateRegistrationNumber()
                ))
                .collect(Collectors.toList());

        return RepresentativeListDto.builder()
                .representativeDto(listRepresentativeDto)
                .build();
    }

    @Transactional
    public RepresentativeSignInResponse loginRepresentative(RepresentativeSignInRequest representativeSignInRequest) {
        Optional<RepresentativeEntity> isRepresentativeExists =
                representativeRepository.findByUsername(representativeSignInRequest.getUsername());

        if (isRepresentativeExists.isEmpty()) {
            return RepresentativeSignInResponse.builder()
                    .isSuccess(false)
                    .message("로그인에 실패했습니다.")
                    .build();
        }

        if (!isRepresentativeExists.get().getPassword()
                .equals(representativeSignInRequest.getPassword())) {
            System.out.println("DB 비밀번호는?" + isRepresentativeExists.get().getPassword());
            System.out.println("로그인 비밀번호는?" + representativeSignInRequest.getPassword());


            return RepresentativeSignInResponse.builder()
                    .isSuccess(false)
                    .message("비밀번호가 다릅니다.")
                    .build();
        }

        if (representativeSignInRequest.getFcmToken() != null) {
            representativeRepository.save(
                    RepresentativeEntity.login()
                            .id(isRepresentativeExists.get().getId())
                            .username(isRepresentativeExists.get().getUsername())
                            .password(isRepresentativeExists.get().getPassword())
                            .fcmToken(representativeSignInRequest.getFcmToken())
                            .corporateRegistrationNumber(isRepresentativeExists.get().getCorporateRegistrationNumber())
                            .build()
            );
        }

        return RepresentativeSignInResponse.builder()
                .isSuccess(true)
                .message("로그인에 성공했습니다.")
                .build();
    }

    public FcmResponse sendByToken(FcmRequest fcmRequest) {
        UserEntity isUserExists = userRepository.findByUserid(fcmRequest.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        itemRepository.findByPicture(fcmRequest.getItemImage())
                .orElseThrow(() -> new IllegalArgumentException("item doesn't exist"));

        try {
            firebaseCloudMessageService.sendMessageTo(
                    isUserExists.getFcmToken(),
                    fcmRequest.getTitle(),
                    fcmRequest.getBody(),
                    fcmRequest.getUserId(),
                    fcmRequest.getTargetId(),
                    fcmRequest.getItemImage(),
                    fcmRequest.getPhase()
            );
        } catch (IOException e) {
            e.printStackTrace();
            return FcmResponse.builder()
                    .message("전송에 실패하였습니다.")
                    .isSuccess(false)
                    .build();
        }
        return FcmResponse.builder()
                .message("전송에 성공하였습니다.")
                .isSuccess(true)
                .build();
    }
}

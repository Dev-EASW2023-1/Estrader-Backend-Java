package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.Realtor.RealtorDto;
import com.example.demo.app.domain.model.dto.Realtor.RealtorListDto;
import com.example.demo.app.domain.model.dto.Realtor.RealtorSignInRequest;
import com.example.demo.app.domain.model.dto.Realtor.RealtorSignInResponse;
import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.entity.RealtorEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.RealtorRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealtorService {

    private final PasswordEncoder passwordEncoder;
    private final RealtorRepository realtorRepository;
    private final UserRepository userRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ItemRepository itemRepository;

    @Transactional
    public void addRealtorInfo(
            String realtorId,
            String password,
            String name,
            String residentNumber,
            String phoneNumber,
            String address,
            String corporateRegistrationNumber,
            String fcmToken,
            String region
    ) {
        RealtorEntity representativeInfo = RealtorEntity.builder()
                .realtorId(realtorId)
                .password(passwordEncoder.encode(password)) // password should be encoded
                .name(name)
                .residentNumber(residentNumber)
                .phoneNumber(phoneNumber)
                .address(address)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .fcmToken(fcmToken)
                .region(region)
                .build();
        realtorRepository.save(representativeInfo);
    }

    public RealtorListDto findRealtorInfo() {
        List<RealtorEntity> listRealtorInfo = realtorRepository.findAll();

        List<RealtorDto> listRealtorDto = listRealtorInfo.stream()
                .map(m -> new RealtorDto(
                        m.getRealtorId(),
                        m.getPassword(),
                        m.getName(),
                        m.getResidentNumber(),
                        m.getPhoneNumber(),
                        m.getAddress(),
                        m.getCorporateRegistrationNumber(),
                        m.getFcmToken(),
                        m.getRegion()
                ))
                .collect(Collectors.toList());

        return RealtorListDto.builder()
                .realtorDto(listRealtorDto)
                .build();
    }

    @Transactional
    public RealtorSignInResponse loginRealtor(RealtorSignInRequest realtorSignInRequest) {
        Optional<RealtorEntity> isRealtorExists =
                realtorRepository.findByRealtorId(realtorSignInRequest.getRealtorId());

        if (isRealtorExists.isEmpty()) {
            return RealtorSignInResponse.builder()
                    .isSuccess(false)
                    .message("로그인에 실패했습니다.")
                    .build();
        }

        if (!passwordEncoder.matches(realtorSignInRequest.getPassword(),
                isRealtorExists.get().getPassword())) {

            System.out.println("DB 비밀번호는?" + isRealtorExists.get().getPassword());
            System.out.println("로그인 비밀번호는?" + realtorSignInRequest.getPassword());

            return RealtorSignInResponse.builder()
                    .isSuccess(false)
                    .message("비밀번호가 다릅니다.")
                    .build();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(realtorSignInRequest.getPassword());

        if (realtorSignInRequest.getFcmToken() != null) {
            realtorRepository.save(
                    RealtorEntity.login()
                            .id(isRealtorExists.get().getId())
                            .realtorId(isRealtorExists.get().getRealtorId())
                            .password(encodedPassword)
                            .name(isRealtorExists.get().getName())
                            .residentNumber(isRealtorExists.get().getResidentNumber())
                            .phoneNumber(isRealtorExists.get().getPhoneNumber())
                            .address(isRealtorExists.get().getAddress())
                            .corporateRegistrationNumber(isRealtorExists.get().getCorporateRegistrationNumber())
                            .fcmToken(realtorSignInRequest.getFcmToken())
                            .region(isRealtorExists.get().getRegion())
                            .build()
            );
        }

        return RealtorSignInResponse.builder()
                .isSuccess(true)
                .message("로그인에 성공했습니다.")
                .build();
    }

    public FcmResponse sendByToken(FcmRequest fcmRequest) {
        UserEntity isUserExists = userRepository.findByUserId(fcmRequest.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        itemRepository.findByPhoto(fcmRequest.getItemImage())
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

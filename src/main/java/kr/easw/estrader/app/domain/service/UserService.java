package kr.easw.estrader.app.domain.service;

import kr.easw.estrader.app.domain.auth.JwtToken;
import com.example.demo.app.domain.exception.exceptions.*;
import kr.easw.estrader.app.domain.exception.exceptions.*;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import kr.easw.estrader.app.domain.model.dto.fcm.FcmRequest;
import kr.easw.estrader.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.dto.user.*;
import kr.easw.estrader.app.domain.model.dto.user.*;
import kr.easw.estrader.app.domain.model.entity.UserEntity;
import kr.easw.estrader.app.domain.repository.ItemRepository;
import kr.easw.estrader.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.easw.estrader.app.domain.Enum.Role.ROLE_MEMBER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ItemRepository itemRepository;
    private final AuthenticationService authenticationService;

    @Transactional
    public void addUserInfo(
            String userid,
            String password,
            String name,
            String residentId,
            String phoneNumber,
            String address,
            String corporateRegistrationNumber,
            String fcmToken,
            String region
    ) {
        UserEntity userinfo = UserEntity.userLogin()
                .userId(userid)
                .password(passwordEncoder.encode(password)) // password should be encoded
                .residentNumber(residentId)
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .fcmToken(fcmToken)
                .region(region)
                .build();
        userRepository.save(userinfo);
    }

    public UserListDto findUserInfo() {
        List<UserEntity> listUserinfo = userRepository.findByRole(ROLE_MEMBER);

        List<UserDto> listUserDto = listUserinfo.stream()
                .map(m -> new UserDto(
                        m.getUserId(),
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

        return UserListDto.builder()
                .userDto(listUserDto)
                .build();
    }

    @Transactional
    public RegisterDataResponse registerUserInfo(RegisterDataRequest registerDataRequest) {
        Optional<UserEntity> isUserExists =
                userRepository.findByUserId(registerDataRequest.getUserId());

        if (isUserExists.isPresent()) {
            log.info("회원가입 실패");
            throw new SignupFailureException(ErrorCode.SIGNUP_FAILURE);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerDataRequest.getPassword());

        userRepository.save(UserEntity.of(
                registerDataRequest,
                encodedPassword
        ));

        log.info("회원가입 성공 = {}", registerDataRequest.getUserId());

        JwtToken token = authenticationService.authenticate(registerDataRequest.getUserId(), registerDataRequest.getPassword());

        return RegisterDataResponse.builder()
                .isSuccess(true)
                .message("회원가입에 성공하였습니다.")
                .token(token.getAccessToken())
                .build();
    }

    public SignupCheckResponse checkDuplicateUserInfo(SignupCheckRequest signupCheckRequest) {
        Optional<UserEntity> isUserExists =
                userRepository.findByUserId(signupCheckRequest.getUserId());

        if (isUserExists.isPresent()) {
            throw new DuplicateIdException(ErrorCode.DUPLICATE_ID);
        }

        log.info("사용할 수 있는 아이디입니다. = {}", signupCheckRequest.getUserId());
        return SignupCheckResponse.builder()
                .isDuplicated(true)
                .message("사용할 수 있는 아이디입니다.")
                .build();
    }

    @Transactional
    public SignInResponse loginUser(SignInRequest signInRequest, JwtToken token) {
        UserEntity isUserExists =
                userRepository.findByUserId(signInRequest.getUserId())
                        .orElseThrow(() -> new LoginFailureException(ErrorCode.LOGIN_FAILURE));

        if (isUserExists.getRole() != ROLE_MEMBER) {
            throw new LoginFailureException(ErrorCode.INSUFFICIENT_PERMISSIONS);
        }

        if (signInRequest.getFcmToken() != null) {
            userRepository.save(UserEntity.of(
                    isUserExists,
                    signInRequest.getFcmToken()
            ));
        }

        log.info("로그인 성공 (구매자) = {}", signInRequest.getUserId());

        return SignInResponse.builder()
                .isSuccess(true)
                .message("로그인에 성공했습니다.")
                .token(token.getAccessToken())
                .build();
    }

    public FcmResponse sendByToken(FcmRequest fcmRequest) {
        UserEntity isUserExists = userRepository.findByUserId(fcmRequest.getTargetId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND2));
        itemRepository.findByPhoto(fcmRequest.getItemImage())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        send(isUserExists.getFcmToken(), fcmRequest);

        log.info("전송 from (구매자) {} to (대리인) {}", fcmRequest.getUserId(), fcmRequest.getTargetId());
        log.info("대리인 토큰 = {}", isUserExists.getFcmToken());

        return FcmResponse.builder()
                .message("전송에 성공하였습니다.")
                .isSuccess(true)
                .build();
    }

    private void send(String targetToken, FcmRequest fcmRequest) {
        try {
            firebaseCloudMessageService.sendMessageTo(
                    targetToken,
                    fcmRequest
            );
        } catch (IOException e) {
            e.printStackTrace();
            log.error("전송 실패");
            throw new FcmFailureException(ErrorCode.FCM_FAILURE);
        }
    }
}

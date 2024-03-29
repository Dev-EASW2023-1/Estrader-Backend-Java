package kr.easw.estrader.app.domain.controller;

import kr.easw.estrader.app.domain.auth.JwtAuthenticateFilter;
import kr.easw.estrader.app.domain.auth.JwtToken;
import kr.easw.estrader.app.domain.model.dto.fcm.FcmRequest;
import kr.easw.estrader.app.domain.model.dto.fcm.FcmResponse;
import kr.easw.estrader.app.domain.model.dto.user.*;
import kr.easw.estrader.app.domain.model.entity.UserEntity;
import kr.easw.estrader.app.domain.repository.UserRepository;
import kr.easw.estrader.app.domain.service.AuthenticationService;
import kr.easw.estrader.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static kr.easw.estrader.app.domain.Enum.Role.ROLE_MEMBER;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService UserService;
    private final AuthenticationService authenticationService;

    // 유저 저장
    @PostMapping("/add")
    public String addUserInfo(
            @RequestParam String userid,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String residentId,
            @RequestParam String phoneNumber,
            @RequestParam String address,
            @RequestParam String corporateRegistrationNumber,
            @RequestParam String fcmToken,
            @RequestParam String region
    ) {
        UserService.addUserInfo(userid, password, name, residentId, phoneNumber, address, corporateRegistrationNumber, fcmToken, region);

        return "redirect:/user/list";
    }

    // 유저 리스트 출력
    @GetMapping("/list")
    public String showListUserinfo(
            Model model
    ) {
        List<UserEntity> userinfoList = userRepository.findByRole(ROLE_MEMBER);
        model.addAttribute("userinfoList", userinfoList);

        return "user/list";
    }



    @GetMapping("/show-list")
    public ResponseEntity<UserListDto> showMember() {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.findUserInfo());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDataResponse> registerMember(
            @RequestBody RegisterDataRequest registerDataRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.registerUserInfo(registerDataRequest));
    }

    @GetMapping("/naver-map")
    public String showNaverMap(
            Model model,
            @AuthenticationPrincipal User user
    ) {
        System.out.println("네이버 지도 확인하는 유저  \n" + user);
        return "item/navermap";
    }

    @PostMapping("/account-exists")
    public ResponseEntity<SignupCheckResponse> checkMember(
            @RequestBody SignupCheckRequest signupCheckRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.checkDuplicateUserInfo(signupCheckRequest));
    }

    @PatchMapping("/login")
    public ResponseEntity<SignInResponse> loginMember(
            @RequestBody SignInRequest signInRequest
    ) {
        JwtToken token = authenticationService.authenticate(signInRequest.getUserId(), signInRequest.getPassword());
        return ResponseEntity.ok()
                .headers(getAuthTokenToHeaders(token))
                .body(UserService.loginUser(signInRequest, token));
    }

    // 테스트용 API (UserDetails 의 username 사용하여 Dto 변환)
    @GetMapping("/test")
    // @AuthenticationPrincipal 사용하여 UserDetailsService 에서 리턴한 객체를 컨트롤러의 파라미터로 직접 참조
    public ResponseEntity<UserDto> getMyUserInfo(
            @AuthenticationPrincipal User user
    ) {
        System.out.println(user.getUsername() + " " + user.getAuthorities());
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(authenticationService.userDetailsToDto(user));
    }

    @PostMapping("/fcm")
    public ResponseEntity<FcmResponse> pushMember(
            @RequestBody FcmRequest fcmRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.sendByToken(fcmRequest));
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }

    private HttpHeaders getAuthTokenToHeaders(JwtToken token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.add(JwtAuthenticateFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());
        return headers;
    }
}

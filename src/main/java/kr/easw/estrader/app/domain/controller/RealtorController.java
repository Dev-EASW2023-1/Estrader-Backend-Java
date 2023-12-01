package kr.easw.estrader.app.domain.controller;

import kr.easw.estrader.app.domain.auth.JwtAuthenticateFilter;
import kr.easw.estrader.app.domain.auth.JwtToken;
import kr.easw.estrader.app.domain.model.dto.Realtor.*;
import kr.easw.estrader.app.domain.model.dto.fcm.FcmRequest;
import kr.easw.estrader.app.domain.model.dto.fcm.FcmResponse;
import kr.easw.estrader.app.domain.model.dto.user.UserDto;
import kr.easw.estrader.app.domain.model.entity.UserEntity;
import kr.easw.estrader.app.domain.repository.UserRepository;
import kr.easw.estrader.app.domain.service.AuthenticationService;
import kr.easw.estrader.app.domain.service.RealtorService;
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

import static kr.easw.estrader.app.domain.Enum.Role.ROLE_MANAGER;

@Controller
@RequestMapping("/realtor")
@RequiredArgsConstructor
public class RealtorController {

    private final RealtorService realtorService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @GetMapping("")
    public String inputPage(){
        return "realtor/index";
    }

    @PostMapping("/add")
    public String addRealtorInfo(
            @RequestParam String realtorId,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String residentNumber,
            @RequestParam String phoneNumber,
            @RequestParam String address,
            @RequestParam String corporateRegistrationNumber,
            @RequestParam String fcmToken,
            @RequestParam String region
    ) {
        realtorService.addRealtorInfo(
                realtorId,
                password,
                name,
                residentNumber,
                phoneNumber,
                address,
                corporateRegistrationNumber,
                fcmToken,
                region
        );

        return "redirect:/realtor/list";
    }

    @GetMapping("/list")
    public String showListRealtorInfo(
            Model model
    ) {
        List<UserEntity> realtorInfoList = userRepository.findByRole(ROLE_MANAGER);
        model.addAttribute("realtorInfoList", realtorInfoList);

        return "realtor/list";
    }

    @GetMapping("/show-list")
    public ResponseEntity<RealtorListDto> showMember() {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(realtorService.findRealtorInfo());
    }

    @PostMapping("/register")
    public ResponseEntity<RealtorRegisterDataResponse> registerMember(
            @RequestBody RealtorRegisterDataRequest realtorRegisterDataRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(realtorService.registerRealtorInfo(realtorRegisterDataRequest));
    }

    @PostMapping("/account-exists")
    public ResponseEntity<RealtorSignupCheckResponse> checkMember(
            @RequestBody RealtorSignupCheckRequest realtorSignupCheckRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(realtorService.checkDuplicateRealtorInfo(realtorSignupCheckRequest));
    }

    @PatchMapping("/login")
    public ResponseEntity<RealtorSignInResponse> loginMember(
            @RequestBody RealtorSignInRequest realtorSignInRequest
    ) {
        JwtToken token = authenticationService.authenticate(realtorSignInRequest.getRealtorId(), realtorSignInRequest.getPassword());
        return ResponseEntity.ok()
                .headers(getAuthTokenToHeaders(token))
                .body(realtorService.loginRealtor(realtorSignInRequest, token));
    }

    @PostMapping("/fcm")
    public ResponseEntity<FcmResponse> pushMember(
            @RequestBody FcmRequest fcmRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(realtorService.sendByToken(fcmRequest));
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

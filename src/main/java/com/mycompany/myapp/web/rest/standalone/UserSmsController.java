package com.mycompany.myapp.web.rest.standalone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.security.UserNotActivatedException;
import com.mycompany.myapp.security.jwt.JWTFilter;
import com.mycompany.myapp.security.jwt.TokenProvider;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.AdminUserDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.sms.builder.SmsBuilder;
import com.mycompany.myapp.sms.model.SmsCode;
import com.mycompany.myapp.sms.model.SmsData;
import com.mycompany.myapp.web.rest.vm.LoginMobileVM;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserSmsController {

    public static final String PARAM_KEY = "code";

    private final TokenProvider tokenProvider;
    private final SmsBuilder smsBuilder;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public UserSmsController(
        TokenProvider tokenProvider,
        SmsBuilder smsBuilder,
        UserDetailsService userDetailsService,
        UserService userService
    ) {
        this.tokenProvider = tokenProvider;
        this.smsBuilder = smsBuilder;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/authenticate/mobile")
    public ResponseEntity<JWTToken> authorizeByMobile(@Valid @RequestBody LoginMobileVM loginMobileVM) {
        // ????????????????????????????????????
        SmsCode smsCode = new SmsCode();
        smsCode.setId(loginMobileVM.getMobile());
        smsCode.setValue(loginMobileVM.getCode());
        boolean result = smsBuilder.template().validateMessage(smsCode);
        // Result<Void> result = daYuSmsService.verify(loginMobileVM.getMobile(), loginMobileVM.getCode(), loginMobileVM.getImageCode(), "jhipsterSampleApplication");
        if (result) {
            // ??????mobile?????????????????????
            Optional<UserDTO> userDTO = userService.findOneByMobile(loginMobileVM.getMobile());
            if (userDTO.isPresent()) {
                Optional<AdminUserDTO> userOptional = userService.getUserWithAuthorities(userDTO.get().getId());
                if (userOptional.isPresent()) {
                    UserDetails user = userDetailsService.loadUserByUsername(userOptional.get().getLogin());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    boolean rememberMe = loginMobileVM.isRememberMe() != null && loginMobileVM.isRememberMe();
                    String jwt = tokenProvider.createTokenWithUserId(authenticationToken, rememberMe, userOptional.get().getId());
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
                    return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
                } else {
                    throw new UsernameNotFoundException("?????????????????????????????????");
                }
            } else {
                throw new UsernameNotFoundException("?????????????????????????????????");
            }
        } else {
            // ?????????
            throw new UserNotActivatedException("???????????????");
        }
    }

    /**
     *
     * ???????????????????????????????????????
     *
     */
    @GetMapping("/mobile/smscode")
    public ResponseEntity<Void> getSmsCode(String mobile, @RequestParam(name = "imageCode", required = false) String imageCode) {
        Optional<UserDTO> userDTO1 = userService.findOneByMobile(mobile);
        if (!userDTO1.isPresent()) {
            // todo??????????????????????????????????????????????????????????????? ???????????????????????????????????????
            AdminUserDTO userDTO = new AdminUserDTO();
            userDTO.setActivated(true);
            userDTO.setLogin(mobile);
            userDTO.setCreatedBy(1L);
            userDTO.setLastModifiedBy(1L);
            userDTO.setEmail(mobile + "@jhipster.pro");
            userDTO.setMobile(mobile);
            User user = userService.createUser(userDTO);
        }
        // ???????????????????????????
        Map<String, String> params = new HashMap<>(1);
        params.put(PARAM_KEY, String.valueOf(RandomUtils.nextInt(100000, 999999)));
        SmsCode smsCode = smsBuilder.template().sendValidate(new SmsData(params).key(PARAM_KEY), mobile);
        if (smsCode.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     *
     * ????????????????????????????????????????????????????????????
     *
     */
    /* @GetMapping("/mobile/smscode/current-user")
    public ResponseEntity<Result<String>> getSmsCodeByCurrentUser(String mobile, @RequestParam(name = "imageCode", required = false) String imageCode) {
        // ??????????????????????????????????????????

        // ????????????????????????????????????????????????

        // ??????????????????????????????person??????
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            Boolean existsMobile = userService.existsByMobileAndLoginNot(mobile, SecurityUtils.getCurrentUserLogin().get());
            if (!existsMobile) {
                // ???????????????????????????
                Result<String> result = daYuSmsService.sendVerifyCode(mobile, "jhipsterSampleApplication");
                if (result.getSuccess()) {
                    return ResponseEntity.ok(result);
                } else {
                    return ResponseEntity.badRequest().body(Result.error(ErrorEnum.UNKNOWN));
                }
            } else {
                return ResponseEntity.badRequest().body(Result.error(ErrorEnum.MOBILE_EXISTED));
            }
        } else {
            return ResponseEntity.badRequest().body(Result.error(ErrorEnum.UNKNOWN));
        }
    } */

    /**
     *
     * ???????????????????????????????????????
     *
     */
    @PostMapping("/mobile/current-user")
    public ResponseEntity<Void> saveMobileByCurrentUser(
        String mobile,
        String code,
        @RequestParam(name = "imageCode", required = false) String imageCode
    ) {
        // ??????
        SmsCode smsCode = new SmsCode();
        smsCode.setId(mobile);
        smsCode.setValue(code);
        boolean result = smsBuilder.template().validateMessage(smsCode);
        if (result) {
            userService.updateUserMobile(mobile);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}

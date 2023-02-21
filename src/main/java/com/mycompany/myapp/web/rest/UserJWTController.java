package com.mycompany.myapp.web.rest;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.enumeration.ApiPermissionState;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.security.annotation.PermissionDefine;
import com.mycompany.myapp.security.jwt.JWTFilter;
import com.mycompany.myapp.security.jwt.TokenProvider;
import com.mycompany.myapp.util.RandImageUtil;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.CommonException;
import com.mycompany.myapp.web.rest.vm.LoginVM;
import io.swagger.annotations.ApiOperation;
import java.nio.charset.StandardCharsets;
import javax.cache.CacheManager;
import javax.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final CacheManager cacheManager;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        CacheManager cacheManager
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/authenticate")
    @PermissionDefine(
        groupName = "系统设置",
        groupCode = "GROUP_SYSTEM",
        entityName = "用户管理",
        entityCode = "USER",
        permissionName = "用户登录加验证码",
        permissionCode = "USER_AUTHENTICATE",
        state = ApiPermissionState.PERMIT_ALL
    )
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        if (StringUtils.isNotBlank(loginVM.getCaptcha()) && StringUtils.isNotBlank(loginVM.getCaptcha())) {
            String lowerCaseCaptcha = loginVM.getCaptcha().toLowerCase();
            String realKey = DigestUtils.md5Hex(lowerCaseCaptcha + loginVM.getCheckKey());
            Object checkCode = cacheManager.getCache(Constants.CAPTCHA_KEY).get(realKey);
            if (checkCode == null || !checkCode.toString().equals(lowerCaseCaptcha)) {
                throw new BadRequestAlertException("验证码错误", "Login", "captchaError");
            }
        } else {
            throw new BadRequestAlertException("验证码错误", "Login", "captchaError");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createTokenWithUserId(
            authentication,
            loginVM.isRememberMe(),
            SecurityUtils.getCurrentUserId().orElse(null)
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/authenticate/withoutCaptcha")
    @PermissionDefine(
        groupName = "系统设置",
        groupCode = "GROUP_SYSTEM",
        entityName = "用户管理",
        entityCode = "USER",
        permissionName = "用户登录无验证码",
        permissionCode = "USER_AUTHENTICATE_WITHOUT_CAPTCHA",
        state = ApiPermissionState.PERMIT_ALL
    )
    public ResponseEntity<JWTToken> authorizeWithoutCaptcha(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createTokenWithUserId(
            authentication,
            loginVM.isRememberMe(),
            SecurityUtils.getCurrentUserId().orElse(null)
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * 后台生成图形验证码 ：有效
     * @param key 前端提供key
     */
    @ApiOperation("获取验证码")
    @GetMapping(value = "/randomImage/{key}")
    @PermissionDefine(
        groupName = "系统设置",
        groupCode = "GROUP_SYSTEM",
        entityName = "用户管理",
        entityCode = "USER",
        permissionName = "获取验证码",
        permissionCode = "USER_CAPTCHA",
        state = ApiPermissionState.PERMIT_ALL
    )
    public ResponseEntity<String> randomImage(@PathVariable String key) {
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = DigestUtils.md5Hex(lowerCaseCode + key);
            cacheManager.getCache(Constants.CAPTCHA_KEY).put(realKey, lowerCaseCode);
            String base64 = RandImageUtil.generate(code);
            return ResponseEntity.ok(base64);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonException("randomImageError", "获取验证码出错");
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

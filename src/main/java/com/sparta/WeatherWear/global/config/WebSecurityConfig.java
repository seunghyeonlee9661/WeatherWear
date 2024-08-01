package com.sparta.WeatherWear.global.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.global.filter.JwtAuthenticationFilter;
import com.sparta.WeatherWear.global.filter.JwtAuthorizationFilter;
import com.sparta.WeatherWear.global.filter.LoginRedirectFilter;
import com.sparta.WeatherWear.global.handler.AccessDeniedHandler;
import com.sparta.WeatherWear.global.handler.AuthenticationEntryPoint;
import com.sparta.WeatherWear.global.handler.AuthenticationSuccessHandler;
import com.sparta.WeatherWear.global.security.JwtUtil;
import com.sparta.WeatherWear.global.security.UserDetailsServiceImpl;
import com.sparta.WeatherWear.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
작성자 : 이승현
JWT 시큐리티 관련
!!!!!!!!!주의!!!!!!!!!!!!!!
이후에 로그인 사용자에 대한 경로 지정 필요!
 */
@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)  // @Secured 애노테이션 활성화
@AllArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final LoginRedirectFilter loginRedirectFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /* 로그인과 JWT 생성을 위한 필터 */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    /* JWT 검증 필터 */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    /* 보안 필터의 범위 설정 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());
        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 접근 가능 범위 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .anyRequest().permitAll()
        );
        // 로그인 관련 경로 설정
//        http.authorizeHttpRequests((authorizeHttpRequests) ->
//                authorizeHttpRequests
//                        // resources 접근 허용 설정
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                        // 로그인 없이 접근 가능한 경로
//                        .requestMatchers(
//                                "/api/login", // 로그인
//                                "/api/signup"
//                        ).permitAll()
//                        .requestMatchers(
//                                HttpMethod.GET,
//                                "/api/teacher", // 강사 정보 불러오기
//                                "/api/teachers", // 강사 목록 불러오기
//                                "/api/lecture", // 강의 정보 불러오기
//                                "/api/lectures", // 강의 목록 불러오기
//                                "/api/products", // 제품 목록 불러오기
//                                "/api/product" // 제품 정보 불러오기
//                        ).permitAll()
//                        // 그 외 모든 요청 인증처리
//                        .anyRequest().authenticated()
//        );

        // 에러 처리를 위한 핸들러 설정
        http.exceptionHandling((exceptionHandling) -> {
            exceptionHandling.accessDeniedHandler(accessDeniedHandler);
            exceptionHandling.authenticationEntryPoint(authenticationEntryPoint);
        });

        // 로그인 처리 진행
        http.formLogin((formLogin) -> formLogin.
                loginPage("/login") // 로그인 페이지 url
                .loginProcessingUrl("/api/login") // 로그인 요청 url
                .successHandler(authenticationSuccessHandler) // 로그인 성공을 처리하는 핸들러
                .permitAll()
        );

        // JWT 검증 및 인가 필터
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        // 로그인 및 JWT 생성 필터
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 로그인 유저 리다이렉트 필터
        http.addFilterBefore(loginRedirectFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* 패스워드 인코딩 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ObjectMapper - 카카오 로그인에 활용
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
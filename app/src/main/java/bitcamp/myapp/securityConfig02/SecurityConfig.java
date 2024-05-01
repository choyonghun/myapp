package bitcamp.myapp.securityConfig02;

import bitcamp.myapp.security.MyUserDetailsService;
import bitcamp.myapp.service.MemberService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// 사용 안하면 밑에 2줄 주석처리해야함!!!!!!!!!!!!
//@Configuration
//@EnableWebSecurity
public class SecurityConfig {

  private static final Log log = LogFactory.getLog(SecurityConfig.class);
  public SecurityConfig() {
    log.debug("SecurityConfig 객체 생성됨!");
  }

  // Spring Security 를 처리할 필터 체인을 준비한다.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // 필터 체인에 들어갈 필터를 설정한다.
    http
        .authorizeHttpRequests((authorize) -> authorize
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
          @Override
          public void customize(FormLoginConfigurer<HttpSecurity> formLoginConfigurer) {
            formLoginConfigurer.loginPage("/auth/form");  // 로그인 폼을 제공하는 URL
            formLoginConfigurer.loginProcessingUrl("/auth/login");     // 로그인을 처리하는 URL
            formLoginConfigurer.usernameParameter("email");      // 로그인을 수행할때 사용할 사용자 아이디(principal) 파라미터 명
            formLoginConfigurer.passwordParameter("password");      // 로그인을 수행할때 사용할 사용자 암호(credential) 파라미터 명
            formLoginConfigurer.defaultSuccessUrl("/home", true);  // 로그인 성공 후 리다이렉트 할 URL
            formLoginConfigurer.permitAll();    // 모든 권한 부여
          }
        });

    // HttpSecurity 객체에 설정한대로 동작할수 있는 필터를 구성한다.
    return http.build();
  }


  // 사용자 정보를 리턴해주는 객체
  @Bean
  public UserDetailsService userDetailsService(MemberService memberService) {
    //  우리가 만든 userDetailsService을 사용한다

    return new MyUserDetailsService(memberService);
  }



}

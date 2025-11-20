package com.suinfinity.user.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.suinfinity.user.model.Authority;
import com.suinfinity.user.util.AuthorityEnum;
import com.suinfinity.user.util.RoleEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${security.role.hierarchy}")
  private String roleHierarchy;

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers("/v1/users")
                    .hasAuthority(AuthorityEnum.ALL.toString())
                    .requestMatchers(
                        PathPatternRequestMatcher.withDefaults().matcher("/v1/users/{name}"))
                    .hasAuthority(AuthorityEnum.ALL.toString())
                    .requestMatchers("/swagger-ui/**", "/api-docs/**")
                    .permitAll()
                    .requestMatchers("/v1/login")
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .formLogin(withDefaults())
        .httpBasic(withDefaults());
    return httpSecurity.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails admin =
        User.withUsername("admin")
            .password(passwordEncoder().encode("Ad$n8admin"))
            .roles(RoleEnum.ADMIN.toString())
            .authorities(Authority.builder().authority(AuthorityEnum.ALL).build())
            .build();

    UserDetails user =
        User.withUsername("user")
            .password(passwordEncoder().encode("user"))
            .roles(RoleEnum.USER.toString())
            .authorities(
                Authority.builder().authority(AuthorityEnum.CREATE_USER).build(),
                Authority.builder().authority(AuthorityEnum.READ_USER).build(),
                Authority.builder().authority(AuthorityEnum.UPDATE_USER).build())
            .build();
    return new InMemoryUserDetailsManager(admin, user);
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(roleHierarchy);
  }
}

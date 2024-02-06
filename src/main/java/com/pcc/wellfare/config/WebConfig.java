//package com.pcc.wellfare.config;
//
//import java.util.Arrays;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//@Configuration
//// Enable Pre & Post Security Annotations
//@EnableWebSecurity
//public class WebConfig extends WebSecurityConfigurerAdapter {
//
//
//  @Autowired
//  JwtTokenProvider jwtTokenProvider;
//
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http
//      .cors()
//      .configurationSource(request -> {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(
//          Arrays.asList("http://localhost:4200")
//        );
//        corsConfiguration.setAllowedHeaders(
//          Arrays.asList(
//            "Origin",
//            "Access-Control-Allow-Origin",
//            "Content-Type",
//            "Accept",
//            "Authorization",
//            "Origin, Accept",
//            "X-Requested-With",
//            "Access-Control-Request-Method",
//            "Access-Control-Request-Headers"
//          )
//        );
//        corsConfiguration.setExposedHeaders(
//          Arrays.asList(
//            "Origin",
//            "Content-Type",
//            "Accept",
//            "Authorization",
//            "Access-Control-Allow-Origin",
//            "Access-Control-Allow-Credentials"
//          )
//        );
//        corsConfiguration.setAllowedMethods(
//          Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
//        );
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration(
//          "/**",
//          corsConfiguration
//        );
//        return corsConfiguration;
//      })
//      .and();
//    http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
//
//    http
//      .authorizeRequests()
//      .antMatchers("/**")
//      .permitAll()
//      //                .antMatchers("/h2-console/**").permitAll()
//      //                .antMatchers("/auth/**", "/auth/login", "/api/users", "/users","/create_user").permitAll()
//      //                .anyRequest().authenticated()
//      .and()
//      .formLogin()
//      .disable()
//      .httpBasic()
//      .disable()
//      .csrf()
//      .disable()
//      .sessionManagement()
//      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//      .and()
//      .headers()
//      .frameOptions()
//      .disable();
//  }
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder(13);
//  }
//
//  @Override
//  @Bean
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
//}

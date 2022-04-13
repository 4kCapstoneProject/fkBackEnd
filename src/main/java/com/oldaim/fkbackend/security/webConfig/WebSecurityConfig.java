package com.oldaim.fkbackend.security.webConfig;

import com.oldaim.fkbackend.security.jwt.CustomFilterExceptionHandler;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticationEntryPoint;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticProvider jwtAuthenticationProvider;
    private final CustomFilterExceptionHandler customFilterExceptionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .cors()

                .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

                .and()
                    .authorizeRequests()
                    .antMatchers("/api/auth/register","/api/auth/login").permitAll()
                    .anyRequest().authenticated()

                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customFilterExceptionHandler,JwtAuthenticationFilter.class);



    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

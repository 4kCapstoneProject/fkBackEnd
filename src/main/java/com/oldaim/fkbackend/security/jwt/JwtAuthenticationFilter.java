package com.oldaim.fkbackend.security.jwt;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticProvider jwtAuthenticProvider;

    private final static String[] ACCEPTED_PATHS
            = new String[]{"/api/auth/register","/api/auth/login","/api/auth/reissueToken"};

    public JwtAuthenticationFilter(JwtAuthenticProvider provider) {
        jwtAuthenticProvider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("필터 동작전");
        if(!isAcceptedPath(request.getRequestURI())) {
            String token = jwtAuthenticProvider.resolveToken(request);

            log.info("token:{}", token);

            if (token != null && jwtAuthenticProvider.validateToken(token)) {
                Authentication authentication = jwtAuthenticProvider.getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAcceptedPath(String path) {
        for (String p : ACCEPTED_PATHS) {
            if (p.equals(path)) return true;
        }
        return false;
    }
}

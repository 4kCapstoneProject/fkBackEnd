package com.oldaim.fkbackend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Log4j2
public class JwtAuthenticProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.access_valid_time}")
    private long ACCESS_VALID_TIME;
    @Value("${jwt.refresh_valid_time}")
    private long REFRESH_VALID_TIME;



    private final UserDetailsService userDetailsService;

    // JWT 토큰 생성
    public String createAccessToken(String userPk, String roles) {
        Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보단위
        claims.put("Auth", roles);
        claims.put("userId",userPk);// 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        return Jwts.builder()
                .setSubject("Access_Token")
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    public String createRefreshToken(String userPk, String roles){

        Date now = new Date();

        return Jwts.builder()
                .setSubject("Refresh_Token")
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + REFRESH_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return (String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("userId");
    }

   //Header에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {

        final String header = request.getHeader("AUTHORIZATION");

        String[] headerString = header.split(" ");

        log.info("헤더 정보:{}",headerString[1]);

        return headerString[1];
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

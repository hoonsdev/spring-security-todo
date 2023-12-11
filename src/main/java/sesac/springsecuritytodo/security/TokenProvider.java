package sesac.springsecuritytodo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sesac.springsecuritytodo.entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
  // jwt 토큰 생성을 위한 비밀키(하드코딩)
  private static final String SECRET_KEY = "sesac-spring-boot-123456";

  // create(): jwt 생성
  public String create(UserEntity userEntity) {
    Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS)); // 지금으로부터 1일

    // jwt 토큰
    // jwt: header, payload, signature
    return Jwts.builder()
        // 헤더에 들어갈 내용 및 서명을 하기 위한 시크릿 키
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        // 페이로드에 들어갈 내용
        .setSubject(String.valueOf(userEntity.getId())) // 토큰 제목
        .setIssuer("spring-security-todo") // iss: 토큰 발급자
        .setIssuedAt(new Date()) // iat: 토큰 발급된 시간
        .setExpiration(expiryDate) // exp: 토큰 만료 시간
        .compact(); // 토큰 생성
  }

  // - 토큰 디코딩 및 파싱하고 토큰 위조여부 확인 -> 사용자 아이디 반환
  // - 라이브러리에서 제공하는 메소드를 사용해 간단히 구축
  public String validateAndGetUserId(String token) {
    // parseClaimsJws(): Base64 디코딩, 파싱
    // - header, payload 를 setSigningKey() 로 넘어온 시크릿 키를 이용해서 서명한 후에 토큰의 서명과 비교
    // - 토큰이 위조되지 않았다고 판별되면, 페이로드(claims) 리턴, 토큰이 위조되었다면? 예외 날림
    // - 그 중 우리는 토큰 유효성 검사 후 유저 아이디를 반환하고자 하니, getBody 호출
    Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }
}
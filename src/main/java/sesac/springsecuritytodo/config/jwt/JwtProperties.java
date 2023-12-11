package sesac.springsecuritytodo.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
// 자바 클래스에 application.properties 파일을 참고해서 가져와서 사용하는 어노테이션
@ConfigurationProperties("jwt")
public class JwtProperties {
  private String issuer;
  private String secretKey;
}

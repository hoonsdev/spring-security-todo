package sesac.springsecuritytodo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sesac.springsecuritytodo.entity.UserEntity;
import sesac.springsecuritytodo.repository.UserRepository;

@Slf4j
@Service
public class UserService {
  @Autowired private UserRepository repository;

  public UserEntity create(final UserEntity userEntity) {
    if (userEntity == null || userEntity.getEmail() == null) {
      throw new RuntimeException("Invalid arguments");
    }

    final String email = userEntity.getEmail();

    if (repository.existsByEmail(email)) {
      log.warn("Email already exists {}", email);
    }

    return repository.save(userEntity); // userEntity 를 DB에 저장
  }

  // [before] 패스워드 암호화 적용 전
  //  public UserEntity getByCredential(final String email, String password) {
  //    return repository.findByEmailAndPassword(email, password);
  //  }

  // [after] 패스워드 암호화 적용 후
  // - BCryptPasswordEncoder 를 이용해 비밀번호 암호화 구현 (같은 비밀번호더라도 인코딩한 결과 값이 달라짐)
  // - 왜? 인코딩할 때마다 의미없는 값을 붙여서 암호화한 결과를 생성하기 때문
  //    - Salt: 의미없는 값
  //    - Salting: Salt 를 붙여서 인코딩하는 것

  public UserEntity getByCredential(
      final String email, final String password, final PasswordEncoder encoder) {
    final UserEntity originalUser = repository.findByEmail(email);

    // matches() 메소드를 이용해서 패스워드 동일 여부 판단
    if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
      return originalUser;
    }

    return null;
  }
}

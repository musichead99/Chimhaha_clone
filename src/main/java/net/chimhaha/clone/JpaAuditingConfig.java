package net.chimhaha.clone;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/* @WevMvcTest를 사용한 컨트롤러 단위 테스트 시 JPA metamodel must not be empty! exception이 발생해 위치 변경 */
@EnableJpaAuditing // jpa auditing기능 설정
@Configuration
public class JpaAuditingConfig {
}

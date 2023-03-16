package net.chimhaha.clone.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스는 테이블과 매핑되지 않지만 해당 클래스를 상속한 자식 클래스에게 매핑 정보를 제공한다.
@EntityListeners({AuditingEntityListener.class}) // 이 클래스에 auditing기능을 제공하는 어노테이션
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}

package cc.gegee.study.week7.practice9.domain;

import cc.gegee.study.week7.base.BaseLongAuditingEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseLongAuditingEntity implements Serializable {

    @Column(length = 20)
    private String mobile;

    @Column(length = 20)
    private String username;
}

package cc.gegee.study.week7.practice10.domain;

import cc.gegee.study.week7.practice10.domain.common.BaseLongAuditingEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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

package cc.gegee.study.week7.practice10.domain;

import cc.gegee.study.week7.practice10.domain.common.BaseLongAuditingEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Commodity extends BaseLongAuditingEntity implements Serializable {

    @Column(length = 50)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
}

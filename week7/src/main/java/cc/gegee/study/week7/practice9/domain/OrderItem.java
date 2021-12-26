package cc.gegee.study.week7.practice9.domain;

import cc.gegee.study.week7.base.BaseLongAuditingEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderItem extends BaseLongAuditingEntity implements Serializable {

    @Column(length = 36)
    private String orderId;

    @Column(length = 36)
    private String commodityId;

    private Integer count;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
}

package cc.gegee.study.week7.practice9.domain;

import cc.gegee.study.week7.base.BaseLongAuditingEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseLongAuditingEntity implements Serializable {

    @Column(precision = 10, scale = 2)
    private BigDecimal money;

    private int status;

    @OneToMany
    private List<OrderItem> items;
}

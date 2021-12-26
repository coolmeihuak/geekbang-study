package cc.page.study.week8;

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
@Table(name = "`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 661434701950670670L;

    @Id
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal money;

    private int status;

    private Long userId;
}

package cc.page.study.week9.practice7.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(length = 50, nullable = false)
    private String num;

    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal money = new BigDecimal("0");

    @Enumerated
    @Builder.Default
    private Type type = Type.RMB;

    public static enum Type {
        USA, RMB
    }
}

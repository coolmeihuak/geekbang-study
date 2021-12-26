package cc.gegee.study.week7.practice10.domain;

import cc.gegee.study.week7.practice10.domain.common.BaseUUID1AuditingEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 设备
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "abpm_device")
public class Device extends BaseUUID1AuditingEntity implements Serializable {

    /**
     * 序列号
     */
    @Column(length = 50, nullable = false, unique = true)
    private String serialNumber;

    /**
     * 设备类型名称
     */
    @Column(length = 50)
    private String deviceCategoryName;
}

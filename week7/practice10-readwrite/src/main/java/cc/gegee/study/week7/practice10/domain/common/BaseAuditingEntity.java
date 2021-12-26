package cc.gegee.study.week7.practice10.domain.common;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * 包含审核信息的基础entiy
 */
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseAuditingEntity {

    /**
     * entity 的创建时间
     */
    @Column(updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdOn;

    /**
     * entity 的最后更新时间
     */
    @Column
    @LastModifiedDate
    private LocalDateTime updatedOn;

    /**
     * 乐观锁的更新版本号 https://docs.jboss.org/hibernate/stable/orm/userguide/html_single/Hibernate_User_Guide.html#locking-optimistic
     */
    @Version
    private Short version;

}

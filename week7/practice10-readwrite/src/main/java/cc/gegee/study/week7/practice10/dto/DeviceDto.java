package cc.gegee.study.week7.practice10.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 设备类型名称
     */
    private String deviceCategoryName;
}

package cc.gegee.study.week7.practice10.model;

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
public class DeviceModel {

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 设备类型名称
     */
    private String deviceCategoryName;
}

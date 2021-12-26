package cc.gegee.study.week7.practice10.service;

import cc.gegee.study.week7.practice10.dto.DeviceDto;
import cc.gegee.study.week7.practice10.model.DeviceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeviceService {

    void add(DeviceModel model);

    void edit(UUID id, DeviceModel model);

    DeviceDto get(UUID id);

    Page<DeviceDto> getPage(Pageable pageable);
}

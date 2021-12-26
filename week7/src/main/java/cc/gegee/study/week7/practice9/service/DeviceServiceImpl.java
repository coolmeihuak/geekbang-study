package cc.gegee.study.week7.practice9.service;

import cc.gegee.study.week7.practice9.db.tags.Master;
import cc.gegee.study.week7.practice9.db.tags.Slave;
import cc.gegee.study.week7.practice9.domain.Device;
import cc.gegee.study.week7.practice9.dto.DeviceDto;
import cc.gegee.study.week7.practice9.model.DeviceModel;
import cc.gegee.study.week7.practice9.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Master
    @Override
    public void add(DeviceModel model) {
        val device = Device.builder()
                .serialNumber(model.getSerialNumber())
                .deviceCategoryName(model.getDeviceCategoryName())
                .build();
        deviceRepository.save(device);
    }

    @Master
    @Override
    public void edit(UUID id, DeviceModel model) {
        val device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid id"));
        device.setSerialNumber(model.getSerialNumber());
        device.setDeviceCategoryName(model.getDeviceCategoryName());
        deviceRepository.save(device);
    }

    @Slave
    @Override
    public DeviceDto get(UUID id) {
        val device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid id"));
        return DeviceDto.builder()
                .serialNumber(device.getSerialNumber())
                .deviceCategoryName(device.getDeviceCategoryName())
                .build();
    }

    @Slave
    @Override
    public Page<DeviceDto> getPage(Pageable pageable) {
        return deviceRepository.findAll(pageable).map(x -> DeviceDto.builder()
                .serialNumber(x.getSerialNumber())
                .deviceCategoryName(x.getDeviceCategoryName())
                .build());
    }

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
}

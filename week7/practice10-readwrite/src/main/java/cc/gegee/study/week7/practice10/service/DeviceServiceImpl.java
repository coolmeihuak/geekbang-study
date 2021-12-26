package cc.gegee.study.week7.practice10.service;

import cc.gegee.study.week7.practice10.domain.Device;
import cc.gegee.study.week7.practice10.dto.DeviceDto;
import cc.gegee.study.week7.practice10.model.DeviceModel;
import cc.gegee.study.week7.practice10.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Override
    public void add(DeviceModel model) {
        val device = Device.builder()
                .serialNumber(model.getSerialNumber())
                .deviceCategoryName(model.getDeviceCategoryName())
                .build();
        deviceRepository.save(device);
    }

    @Override
    public void edit(UUID id, DeviceModel model) {
        val device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid id"));
        device.setSerialNumber(model.getSerialNumber());
        device.setDeviceCategoryName(model.getDeviceCategoryName());
        deviceRepository.save(device);
    }

    @Override
    public DeviceDto get(UUID id) {
        val device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid id"));
        return DeviceDto.builder()
                .serialNumber(device.getSerialNumber())
                .deviceCategoryName(device.getDeviceCategoryName())
                .build();
    }

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

package cc.gegee.study.week7.practice9.web;

import cc.gegee.study.week7.practice9.dto.DeviceDto;
import cc.gegee.study.week7.practice9.model.DeviceModel;
import cc.gegee.study.week7.practice9.service.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @PostMapping
    public HttpEntity<Void> add(@RequestBody DeviceModel model) {
        deviceService.add(model);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public HttpEntity<Void> edit(@PathVariable UUID id, @RequestBody DeviceModel model) {
        deviceService.edit(id, model);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public HttpEntity<DeviceDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.get(id));
    }

    @GetMapping
    public HttpEntity<Page<DeviceDto>> getPage(Pageable pageable) {
        return ResponseEntity.ok(deviceService.getPage(pageable));
    }

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
}

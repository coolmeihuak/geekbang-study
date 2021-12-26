package cc.gegee.study.week7.practice10.repository;

import cc.gegee.study.week7.practice10.domain.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends BaseRepository<Device, UUID> {

    Optional<Device> findBySerialNumber(String serialNumber);
}

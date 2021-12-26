package cc.gegee.study.week7.practice9.repository;

import cc.gegee.study.week7.practice9.domain.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends BaseRepository<Device, UUID> {

    Optional<Device> findBySerialNumber(String serialNumber);
}

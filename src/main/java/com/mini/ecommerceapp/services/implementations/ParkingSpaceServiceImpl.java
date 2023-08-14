package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.ParkingSpaceRepository;
import com.mini.ecommerceapp.services.OrderService;
import com.mini.ecommerceapp.services.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.mini.ecommerceapp.utils.Constants.PARKING_SPACE_NOT_FOUND;
import static com.mini.ecommerceapp.utils.Constants.calculateSlots;

@Service
@Transactional
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final OrderService orderService;

    @Autowired
    public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository, OrderService orderService) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.orderService = orderService;
    }

    @Override
    public List<ParkingSpace> getAllParkingSpace() {
        return parkingSpaceRepository.findAll();
    }

    @Override
    public ParkingSpace getParkingSpace(String name) {
        return parkingSpaceRepository.getByName(name).orElseThrow(() -> new ResourceNotFoundException(PARKING_SPACE_NOT_FOUND));
    }

    @Override
    public ParkingSpace getParkingSpace(long id) {
        return parkingSpaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PARKING_SPACE_NOT_FOUND));
    }

    @Override
    public ParkingSpace getParkingSpace(LocalDateTime startTime, LocalDateTime endTime, long id) {
        ParkingSpace parkingSpace = getParkingSpace(id);
        if (startTime != null || endTime != null) {
            Map<Long, Long> map = orderService.getOrderCountForParkingSpace(startTime, endTime, id);
            calculateSlots(map, parkingSpace);
        }
        return parkingSpace;
    }

    @Override
    public ParkingSpace saveParkingSpace(ParkingSpace parkingSpace) {
        return parkingSpaceRepository.save(parkingSpace);
    }

    @Override
    public ParkingSpace updateParkingSpace(ParkingSpace parkingSpace) {
        checkID(parkingSpace.getId());
        return parkingSpaceRepository.save(parkingSpace);
    }

    @Override
    public void deleteParkingSpace(long id) {
        checkID(id);
        parkingSpaceRepository.deleteById(id);
    }

    @Override
    public void addVehicularSpaceToParkingSpace(String name, VehicularSpace space) {
        getParkingSpace(name).getVehicularSpaces().add(space);
    }

    private void checkID(long id) {
        if (!parkingSpaceRepository.existsById(id)) {
            throw new ResourceNotFoundException(PARKING_SPACE_NOT_FOUND);
        }
    }
}

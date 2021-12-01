package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.ParkingSpaceRepository;
import com.mini.ecommerceapp.services.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    @Override
    public List<ParkingSpace> getAllParkingSpace() {
        return parkingSpaceRepository.findAll();
    }

    @Override
    public ParkingSpace getParkingSpace(String name) {
        return parkingSpaceRepository.getByName(name).orElseThrow(() -> new ResourceNotFoundException("Parking Space Not Found"));
    }

    @Override
    public ParkingSpace getParkingSpace(long id) {
        return parkingSpaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Parking Space Not Found"));
    }

    @Override
    public ParkingSpace saveParkingSpace(ParkingSpace parkingSpace) {
        return parkingSpaceRepository.save(parkingSpace);
    }

    @Override
    public void addVehicularSpaceToParkingSpace(String name, VehicularSpace space) {
        getParkingSpace(name).getVehicularSpaces().add(space);
    }
}

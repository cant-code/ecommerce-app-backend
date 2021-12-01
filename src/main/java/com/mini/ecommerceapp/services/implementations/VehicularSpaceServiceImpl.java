package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.VehicularSpaceRepository;
import com.mini.ecommerceapp.services.VehicularSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VehicularSpaceServiceImpl implements VehicularSpaceService {
    private final VehicularSpaceRepository vehicularSpaceRepository;

    @Autowired
    public VehicularSpaceServiceImpl(VehicularSpaceRepository vehicularSpaceRepository) {
        this.vehicularSpaceRepository = vehicularSpaceRepository;
    }

    @Override
    public List<VehicularSpace> getAllVehicularSpace() {
        return vehicularSpaceRepository.findAll();
    }

    @Override
    public VehicularSpace getVehicularSpace(String s, String name) {
        return vehicularSpaceRepository.getByNameAndParkingSpace_Name(name, s).orElseThrow(() -> new ResourceNotFoundException("Vehicle Space Not Found"));
    }

    @Override
    public VehicularSpace getVehicularSpace(long id) {
        return vehicularSpaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle Space Not Found"));
    }

    @Override
    public VehicularSpace saveVehicularSpace(VehicularSpace vehicularSpace) {
        return vehicularSpaceRepository.save(vehicularSpace);
    }
}

package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.repository.AreaRepository;
import com.mini.ecommerceapp.services.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public Area saveArea(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public Area getArea(String name) {
        return areaRepository.getByName(name).orElseThrow(() -> new ResourceNotFoundException("Area Not Found"));
    }

    @Override
    public Area getArea(long id) {
        return areaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Area Not Found"));
    }

    @Override
    public List<Area> search(String s) {
        return areaRepository.findByNameContainingIgnoreCase(s);
    }

    @Override
    public List<Area> getAreas() {
        return areaRepository.findAll();
    }

    @Override
    public void addParkingSpace(String name, ParkingSpace parkingSpace) {
        getArea(name).getParkingSlots().add(parkingSpace);
    }
}

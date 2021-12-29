package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.dto.SearchDTO;
import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.repository.AreaRepository;
import com.mini.ecommerceapp.services.AreaService;
import com.mini.ecommerceapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;
    private final OrderService orderService;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository, OrderService orderService) {
        this.areaRepository = areaRepository;
        this.orderService = orderService;
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
    public List<Area> search(SearchDTO s) {
        Map<Long, Long> orderMap = orderService.getOrderCount(s.getStartTimeStamp(), s.getEndTimeStamp());
        List<Area> areas = areaRepository.findByNameContainingIgnoreCase(s.getSearch());
        areas.forEach(area -> area.getParkingSlots().forEach(
                parkingSpace -> {
                    parkingSpace.getVehicularSpaces().forEach(
                            vehicularSpace -> {
                                long total = vehicularSpace.getTotalSlots();
                                long slots = orderMap.containsKey(vehicularSpace.getId()) ?
                                        total - orderMap.get(vehicularSpace.getId()) : total;
                                vehicularSpace.setAvailableSlots(slots);
                            }
                    );
                    parkingSpace.getVehicularSpaces().removeIf(vehicularSpace -> vehicularSpace.getAvailableSlots() == 0);
                }
        ));
        return areas;
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

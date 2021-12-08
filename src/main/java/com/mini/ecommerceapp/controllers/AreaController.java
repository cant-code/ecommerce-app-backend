package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.SearchDTO;
import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.services.AreaService;
import com.mini.ecommerceapp.services.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.mini.ecommerceapp.utils.Constants.AREA;

@RestController
@RequestMapping(AREA)
public class AreaController {
    private final AreaService areaService;
    private final ParkingSpaceService parkingSpaceService;

    @Autowired
    public AreaController(AreaService areaService, ParkingSpaceService parkingSpaceService) {
        this.areaService = areaService;
        this.parkingSpaceService = parkingSpaceService;
    }

    @GetMapping("")
    public List<Area> getAllArea() {
        return areaService.getAreas();
    }

    @GetMapping("/byId/{id}")
    public Area getArea(@Valid @PathVariable long id) {
        return areaService.getArea(id);
    }

    @GetMapping("/byName/{name}")
    public Area getArea(@Valid @PathVariable String name) {
        return areaService.getArea(name);
    }

    @PostMapping("/add")
    public Area addArea(@Valid @RequestBody Area area) {
        return areaService.saveArea(area);
    }

    @GetMapping("/search")
    public List<Area> searchArea(@Valid SearchDTO area) {
        return areaService.search(area);
    }

    @PostMapping("/parkingspace/add")
    public void addParkingSpaceToArea(@NotNull @RequestBody Map<String, String> data) {
        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(data.get("parkingspace"));
        areaService.addParkingSpace(data.get("area"), parkingSpace);
    }
}

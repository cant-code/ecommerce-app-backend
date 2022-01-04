package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.services.ParkingSpaceService;
import com.mini.ecommerceapp.services.VehicularSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.PARKING_SPACE;

@RestController
@RequestMapping(PARKING_SPACE)
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;
    private final VehicularSpaceService vehicularSpaceService;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService, VehicularSpaceService vehicularSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
        this.vehicularSpaceService = vehicularSpaceService;
    }

    @GetMapping("")
    public List<ParkingSpace> getAllParkingSpace() {
        return parkingSpaceService.getAllParkingSpace();
    }

    @GetMapping("/{id}")
    public ParkingSpace getParkingSpace(@PathVariable long id,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                LocalDateTime startDate,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                    LocalDateTime endDate) {
        return parkingSpaceService.getParkingSpace(startDate, endDate, id);
    }

    @GetMapping("/byName/{name}")
    public ParkingSpace getParkingSpace(@PathVariable String name) {
        return parkingSpaceService.getParkingSpace(name);
    }

    @PostMapping("/add")
    public ParkingSpace addParkingSpace(@Valid @RequestBody ParkingSpace space) {
        return parkingSpaceService.saveParkingSpace(space);
    }

    @PutMapping("/edit")
    public ParkingSpace editParkingSpace(@Valid @RequestBody ParkingSpace space) {
        return parkingSpaceService.updateParkingSpace(space);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteParkingSpace(@Valid @PathVariable long id) {
        parkingSpaceService.deleteParkingSpace(id);
    }

    @PostMapping("/byName/{name}/vehicularspace/add")
    public VehicularSpace addVehicularSpace(@Valid @PathVariable String name, @Valid @RequestBody VehicularSpace vehicularSpace) {
        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(name);
        vehicularSpace.setParkingSpace(parkingSpace);
        return vehicularSpaceService.saveVehicularSpace(vehicularSpace);
    }

    @PostMapping("/{id}/vehicularspace/add")
    public VehicularSpace addVehicularSpace(@Valid @PathVariable long id, @Valid @RequestBody VehicularSpace vehicularSpace) {
        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(id);
        vehicularSpace.setParkingSpace(parkingSpace);
        return vehicularSpaceService.saveVehicularSpace(vehicularSpace);
    }
}

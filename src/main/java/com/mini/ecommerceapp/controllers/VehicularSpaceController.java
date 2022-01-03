package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.services.VehicularSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.VEHICLE_SPACE;

@RestController
@RequestMapping(VEHICLE_SPACE)
public class VehicularSpaceController {
    private final VehicularSpaceService vehicularSpaceService;

    @Autowired
    public VehicularSpaceController(VehicularSpaceService vehicularSpaceService) {
        this.vehicularSpaceService = vehicularSpaceService;
    }

    @GetMapping("")
    public List<VehicularSpace> getAllVehicularSpace() {
        return vehicularSpaceService.getAllVehicularSpace();
    }

    @GetMapping("/{id}")
    public VehicularSpace getVehicularSpace(@Valid @PathVariable long id) {
        return vehicularSpaceService.getVehicularSpace(id);
    }

    @PutMapping("/edit")
    public VehicularSpace updateVehicularSpace(@Valid @RequestBody VehicularSpace vehicularSpace) {
        return vehicularSpaceService.updateVehicularSpace(vehicularSpace);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteVehicularSpace(@Valid @PathVariable long id) {
        vehicularSpaceService.deleteVehicularSpace(id);
    }

    @GetMapping("/{parkingSpace}/{name}")
    public VehicularSpace getVehicularSpace(@Valid @PathVariable String parkingSpace, @Valid @PathVariable String name) {
        return vehicularSpaceService.getVehicularSpace(parkingSpace, name);
    }
}

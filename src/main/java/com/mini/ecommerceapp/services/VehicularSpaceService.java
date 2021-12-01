package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.models.VehicularSpace;

import java.util.List;

public interface VehicularSpaceService {
    List<VehicularSpace> getAllVehicularSpace();
    VehicularSpace getVehicularSpace(String s, String name);
    VehicularSpace getVehicularSpace(long id);
    VehicularSpace saveVehicularSpace(VehicularSpace vehicularSpace);
}

package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;

import java.util.List;

public interface ParkingSpaceService {
    List<ParkingSpace> getAllParkingSpace();
    ParkingSpace getParkingSpace(String name);
    ParkingSpace getParkingSpace(long id);
    ParkingSpace saveParkingSpace(ParkingSpace parkingSpace);
    void addVehicularSpaceToParkingSpace(String name, VehicularSpace space);
}
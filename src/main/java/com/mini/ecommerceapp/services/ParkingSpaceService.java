package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingSpaceService {
    List<ParkingSpace> getAllParkingSpace();
    ParkingSpace getParkingSpace(String name);
    ParkingSpace getParkingSpace(long id);
    ParkingSpace getParkingSpace(LocalDateTime startTime, LocalDateTime endTime, long id);
    ParkingSpace saveParkingSpace(ParkingSpace parkingSpace);
    void addVehicularSpaceToParkingSpace(String name, VehicularSpace space);
}
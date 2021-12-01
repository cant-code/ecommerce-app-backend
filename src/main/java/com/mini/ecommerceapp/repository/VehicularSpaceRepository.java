package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.VehicularSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicularSpaceRepository extends JpaRepository<VehicularSpace, Long> {
    Optional<VehicularSpace> getByName(String name);
    Optional<VehicularSpace> getByNameAndParkingSpace_Name(String name, String parkingSpace_Name);
}

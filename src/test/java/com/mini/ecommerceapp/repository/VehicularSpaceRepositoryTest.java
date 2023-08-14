package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.utils.MockData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicularSpaceRepositoryTest {

    @Autowired
    private VehicularSpaceRepository vehicularSpaceRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Test
    @DisplayName("Find Vehicular Space using Name and Parking Space Name")
    void findVehicularSpaceByNameAndParkingSpaceName() {
        ParkingSpace parkingSpace = MockData.buildParkingSpace();
        parkingSpaceRepository.save(parkingSpace);

        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();
        vehicularSpace.setParkingSpace(parkingSpace);

        vehicularSpaceRepository.save(vehicularSpace);

        Optional<VehicularSpace> optionalVehicularSpace = vehicularSpaceRepository
                .getByNameAndParkingSpace_Name(vehicularSpace.getName(), parkingSpace.getName());

        assertTrue(optionalVehicularSpace.isPresent());
        assertEquals(vehicularSpace, optionalVehicularSpace.get());
    }

    @Test
    @DisplayName("Dont Find Vehicular Space using Name and Parking Space Name")
    void dontFindVehicularSpaceByNameAndParkingSpaceName() {
        Optional<VehicularSpace> optionalVehicularSpace = vehicularSpaceRepository
                .getByNameAndParkingSpace_Name(RandomStringUtils.random(20), RandomStringUtils.random(20));

        assertFalse(optionalVehicularSpace.isPresent());
    }
}
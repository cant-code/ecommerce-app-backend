package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.ParkingSpace;
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
class ParkingSpaceRepositoryTest {

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Test
    @DisplayName("Find Parking Space using name")
    void getByName() {
        ParkingSpace parkingSpaceData = MockData.buildParkingSpace();

        parkingSpaceRepository.save(parkingSpaceData);

        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.getByName(parkingSpaceData.getName());
        assertTrue(parkingSpace.isPresent());
        assertEquals(parkingSpaceData, parkingSpace.get());
    }

    @Test
    @DisplayName("Dont Find Parking Space using name")
    void dontGetByName() {
        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.getByName(RandomStringUtils.random(20));
        assertFalse(parkingSpace.isPresent());
    }
}
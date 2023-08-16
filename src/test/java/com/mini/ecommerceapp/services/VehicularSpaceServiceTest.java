package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.VehicularSpaceRepository;
import com.mini.ecommerceapp.services.implementations.VehicularSpaceServiceImpl;
import com.mini.ecommerceapp.utils.MockData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.mini.ecommerceapp.utils.Constants.VEHICLE_SPACE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicularSpaceServiceTest {

    private VehicularSpaceService vehicularSpaceService;

    @Mock
    private VehicularSpaceRepository vehicularSpaceRepository;

    @BeforeEach
    void init() {
        vehicularSpaceService = new VehicularSpaceServiceImpl(vehicularSpaceRepository);
    }

    @Test
    @DisplayName("Get all vehicular space")
    void getAllVehicularSpace() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        when(vehicularSpaceRepository.findAll()).thenReturn(List.of(vehicularSpace));

        List<VehicularSpace> vehicularSpaces = vehicularSpaceService.getAllVehicularSpace();

        assertEquals(1, vehicularSpaces.size());
        assertEquals(vehicularSpace, vehicularSpaces.get(0));
    }

    @Test
    @DisplayName("Get vehicular space using name and parking space name")
    void getVehicularSpaceUsingName() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        when(vehicularSpaceRepository.getByNameAndParkingSpace_Name(anyString(), anyString()))
                .thenReturn(Optional.of(vehicularSpace));

        VehicularSpace vehicularSpace1 = vehicularSpaceService
                .getVehicularSpace(RandomStringUtils.random(10), RandomStringUtils.random(10));

        assertEquals(vehicularSpace, vehicularSpace1);
    }

    @Test
    @DisplayName("Dont get vehicular space using name and parking space name")
    void dontGetVehicularSpaceUsingName() {

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> vehicularSpaceService.getVehicularSpace("test1", "test2"));

        assertEquals(VEHICLE_SPACE_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Get vehicular space using id")
    void getVehicularSpaceUsingId() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        when(vehicularSpaceRepository.findById(anyLong())).thenReturn(Optional.of(vehicularSpace));

        VehicularSpace vehicularSpace1 = vehicularSpaceService.getVehicularSpace(RandomUtils.nextLong());

        assertEquals(vehicularSpace, vehicularSpace1);
    }

    @Test
    @DisplayName("Dont get vehicular space using id")
    void dontGetVehicularSpaceUsingId() {

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> vehicularSpaceService.getVehicularSpace(1L));

        assertEquals(VEHICLE_SPACE_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Save vehicular space")
    void saveVehicularSpace() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        when(vehicularSpaceRepository.save(vehicularSpace)).thenReturn(vehicularSpace);

        VehicularSpace vehicularSpace1 = vehicularSpaceService.saveVehicularSpace(vehicularSpace);

        assertEquals(vehicularSpace, vehicularSpace1);
    }

    @Test
    @DisplayName("Update vehicular space")
    void updateVehicularSpace() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        when(vehicularSpaceRepository.existsById(anyLong())).thenReturn(true);
        when(vehicularSpaceRepository.save(vehicularSpace)).thenReturn(vehicularSpace);

        VehicularSpace vehicularSpace1 = vehicularSpaceService.updateVehicularSpace(vehicularSpace);

        assertEquals(vehicularSpace, vehicularSpace1);
    }

    @Test
    @DisplayName("Dont update vehicular space")
    void dontUpdateVehicularSpace() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        when(vehicularSpaceRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> vehicularSpaceService.updateVehicularSpace(vehicularSpace));

        assertEquals(VEHICLE_SPACE_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Delete vehicular space")
    void deleteVehicularSpace() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        when(vehicularSpaceRepository.existsById(anyLong())).thenReturn(true);

        vehicularSpaceService.deleteVehicularSpace(vehicularSpace.getId());

        verify(vehicularSpaceRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Dont delete vehicular space")
    void dontDeleteVehicularSpace() {
        when(vehicularSpaceRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> vehicularSpaceService.deleteVehicularSpace(1L));

        assertEquals(VEHICLE_SPACE_NOT_FOUND, e.getMessage());
    }
}
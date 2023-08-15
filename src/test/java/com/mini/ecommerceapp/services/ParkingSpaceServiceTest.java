package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.ParkingSpaceRepository;
import com.mini.ecommerceapp.services.implementations.ParkingSpaceServiceImpl;
import com.mini.ecommerceapp.utils.MockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mini.ecommerceapp.utils.Constants.PARKING_SPACE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpaceServiceTest {

    private ParkingSpaceService parkingSpaceService;

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void init() {
        parkingSpaceService = new ParkingSpaceServiceImpl(parkingSpaceRepository, orderService);
    }

    @Test
    @DisplayName("Get all parking space")
    void getAllParkingSpace() {
        List<ParkingSpace> parkingSpaces = List.of(MockData.buildParkingSpace());

        when(parkingSpaceRepository.findAll()).thenReturn(parkingSpaces);

        List<ParkingSpace> parkingSpaceList = parkingSpaceService.getAllParkingSpace();

        assertEquals(parkingSpaces, parkingSpaceList);
    }

    @Test
    @DisplayName("Get parking space by name")
    void getParkingSpaceByName() {
        ParkingSpace mockData = MockData.buildParkingSpace();

        when(parkingSpaceRepository.getByName(anyString())).thenReturn(Optional.of(mockData));

        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(mockData.getName());

        assertEquals(mockData, parkingSpace);
    }

    @Test
    @DisplayName("Dont Get parking space by name")
    void dontGetParkingSpaceByName() {

        when(parkingSpaceRepository.getByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> parkingSpaceService.getParkingSpace("test"));

        assertEquals(PARKING_SPACE_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Get parking space by id")
    void getParkingSpaceById() {
        ParkingSpace mockData = MockData.buildParkingSpace();

        when(parkingSpaceRepository.findById(anyLong())).thenReturn(Optional.of(mockData));

        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(mockData.getId());

        assertEquals(mockData, parkingSpace);
    }

    @Test
    @DisplayName("Dont Get parking space by id")
    void dontGetParkingSpaceById() {

        when(parkingSpaceRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> parkingSpaceService.getParkingSpace(1L));

        assertEquals(PARKING_SPACE_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Get parking space by id")
    void getParkingSpace() {
        ParkingSpace mockData = MockData.buildParkingSpace();

        List<VehicularSpace> vehicularSpaces = new ArrayList<>();
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();
        vehicularSpace.setId(1L);
        vehicularSpaces.add(vehicularSpace);
        mockData.setVehicularSpaces(vehicularSpaces);

        when(parkingSpaceRepository.findById(anyLong())).thenReturn(Optional.of(mockData));
        when(orderService.getOrderCountForParkingSpace(any(LocalDateTime.class), any(LocalDateTime.class), anyLong()))
                .thenReturn(Map.of(1L, 1L));

        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(LocalDateTime.now(), LocalDateTime.now(), mockData.getId());

        assertEquals(mockData, parkingSpace);
    }

    @Test
    @DisplayName("Save parking space")
    void saveParkingSpace() {
        ParkingSpace parkingSpace = MockData.buildParkingSpace();

        when(parkingSpaceRepository.save(any())).thenReturn(parkingSpace);

        ParkingSpace space = parkingSpaceService.saveParkingSpace(parkingSpace);

        assertEquals(parkingSpace, space);
    }

    @Test
    @DisplayName("Update parking space by id")
    void updateParkingSpaceById() {
        ParkingSpace mockParkingSpace = MockData.buildParkingSpace();

        when(parkingSpaceRepository.existsById(anyLong())).thenReturn(true);
        when(parkingSpaceRepository.save(any(ParkingSpace.class))).thenReturn(mockParkingSpace);

        ParkingSpace parkingSpace = parkingSpaceService.updateParkingSpace(mockParkingSpace);

        assertNotNull(parkingSpace);
        assertEquals(mockParkingSpace, parkingSpace);
    }

    @Test
    @DisplayName("Dont update parking space by id")
    void dontUpdateParkingSpaceById() {
        ParkingSpace mockParkingSpace = MockData.buildParkingSpace();

        when(parkingSpaceRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> parkingSpaceService.updateParkingSpace(mockParkingSpace));

        assertEquals(PARKING_SPACE_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Delete parking space by id")
    void deleteParkingSpaceById() {
        when(parkingSpaceRepository.existsById(anyLong())).thenReturn(true);

        parkingSpaceService.deleteParkingSpace(1L);

        verify(parkingSpaceRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Dont delete parking space by id")
    void dontDeleteParkingSpaceById() {
        when(parkingSpaceRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> parkingSpaceService.deleteParkingSpace(1L));

        assertEquals(PARKING_SPACE_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Add vehicular space to parking space")
    void addVehicularSpaceToParkingSpace() {
        ParkingSpace mockData = MockData.buildParkingSpace();

        List<VehicularSpace> vehicularSpaces = new ArrayList<>();
        mockData.setVehicularSpaces(vehicularSpaces);

        when(parkingSpaceRepository.getByName(anyString())).thenReturn(Optional.of(mockData));

        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();
        parkingSpaceService.addVehicularSpaceToParkingSpace(mockData.getName(), vehicularSpace);

        verify(parkingSpaceRepository, times(1)).getByName(anyString());

        assertTrue(mockData.getVehicularSpaces().contains(vehicularSpace));
    }

    @Test
    @DisplayName("Dont Add vehicular space to parking space")
    void dontAddVehicularSpaceToParkingSpace() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> parkingSpaceService.addVehicularSpaceToParkingSpace("test", vehicularSpace));

        assertEquals(PARKING_SPACE_NOT_FOUND, e.getMessage());
    }
}
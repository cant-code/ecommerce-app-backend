package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.dto.SearchDTO;
import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.AreaRepository;
import com.mini.ecommerceapp.services.implementations.AreaServiceImpl;
import com.mini.ecommerceapp.utils.MockData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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

import static com.mini.ecommerceapp.utils.Constants.AREA_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AreaServiceTest {

    private AreaService areaService;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void init() {
        areaService = new AreaServiceImpl(areaRepository, orderService);
    }

    @Test
    @DisplayName("Save area")
    void saveArea() {
        Area mockArea = MockData.buildArea();

        when(areaRepository.save(any(Area.class))).thenReturn(mockArea);

        Area area = areaService.saveArea(mockArea);

        assertNotNull(area);
        assertEquals(mockArea, area);
    }

    @Test
    @DisplayName("Get area by name")
    void getAreaByName() {
        Area mockArea = MockData.buildArea();

        when(areaRepository.getByName(anyString())).thenReturn(Optional.of(mockArea));

        Area area = areaService.getArea(RandomStringUtils.random(20));

        assertNotNull(area);
        assertEquals(mockArea, area);
    }

    @Test
    @DisplayName("Dont get area by name")
    void dontGetAreaByName() {
        when(areaRepository.getByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> areaService.getArea("Test"));

        assertEquals(AREA_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Get area by id")
    void getAreaById() {
        Area mockArea = MockData.buildArea();

        when(areaRepository.findById(anyLong())).thenReturn(Optional.of(mockArea));

        Area area = areaService.getArea(RandomUtils.nextLong());

        assertNotNull(area);
        assertEquals(mockArea, area);
    }

    @Test
    @DisplayName("Dont get area by id")
    void dontGetAreaById() {
        when(areaRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> areaService.getArea(1));

        assertEquals(AREA_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Update area by id")
    void updateAreaById() {
        Area mockArea = MockData.buildArea();

        when(areaRepository.existsById(anyLong())).thenReturn(true);
        when(areaRepository.save(any(Area.class))).thenReturn(mockArea);

        Area area = areaService.updateArea(mockArea);

        assertNotNull(area);
        assertEquals(mockArea, area);
    }

    @Test
    @DisplayName("Dont update area by id")
    void dontUpdateAreaById() {
        when(areaRepository.existsById(anyLong())).thenReturn(false);

        Area area = MockData.buildArea();
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> areaService.updateArea(area));

        assertEquals(AREA_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Delete area by id")
    void deleteAreaById() {
        Area mockArea = MockData.buildArea();

        when(areaRepository.existsById(anyLong())).thenReturn(true);

        areaService.deleteArea(mockArea.getId());

        verify(areaRepository, times(1)).deleteById(mockArea.getId());
    }

    @Test
    @DisplayName("Dont delete area by id")
    void dontDeleteAreaById() {
        when(areaRepository.existsById(anyLong())).thenReturn(false);

        Area area = MockData.buildArea();
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> areaService.updateArea(area));

        assertEquals(AREA_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Search for area")
    void searchArea() {
        Area mockArea = MockData.buildArea();

        List<VehicularSpace> vehicularSpaces = new ArrayList<>();
        vehicularSpaces.add(MockData.buildVehicularSpace());
        mockArea.getParkingSlots().get(0).setVehicularSpaces(vehicularSpaces);

        when(orderService.getOrderCount(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Map.of(mockArea.getId(), 1L));
        when(areaRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of(mockArea));

        List<Area> areas = areaService
                .search(new SearchDTO(LocalDateTime.now(), LocalDateTime.now(), RandomStringUtils.random(20)));

        assertEquals(1, areas.size());
        assertEquals(mockArea, areas.get(0));
        assertEquals(mockArea.getParkingSlots().get(0).getVehicularSpaces().get(0).getTotalSlots() - 1,
                areas.get(0).getParkingSlots().get(0).getVehicularSpaces().get(0).getAvailableSlots());
    }

    @Test
    @DisplayName("Get All Areas")
    void getAllAreas() {
        List<Area> areas = List.of(MockData.buildArea());

        when(areaRepository.findAll()).thenReturn(areas);

        List<Area> areaList = areaService.getAreas();

        assertEquals(areas, areaList);
    }

    @Test
    @DisplayName("Add parking space to Area")
    void addParkingSpaceToArea() {
        Area area = MockData.buildArea();
        List<ParkingSpace> parkingSpaces = new ArrayList<>();
        area.setParkingSlots(parkingSpaces);

        when(areaRepository.getByName(anyString())).thenReturn(Optional.of(area));

        ParkingSpace parkingSpace = MockData.buildParkingSpace();
        areaService.addParkingSpace(area.getName(), parkingSpace);

        verify(areaRepository, times(1)).getByName(anyString());

        assertTrue(area.getParkingSlots().contains(parkingSpace));
    }

    @Test
    @DisplayName("Dont Add parking space to Area")
    void dontAddParkingSpaceToArea() {
        ParkingSpace parkingSpace = MockData.buildParkingSpace();
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> areaService.addParkingSpace("test", parkingSpace));

        assertEquals(AREA_NOT_FOUND, e.getMessage());
    }
}
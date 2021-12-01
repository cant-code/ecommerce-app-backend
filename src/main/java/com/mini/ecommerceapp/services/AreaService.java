package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.models.ParkingSpace;

import java.util.List;

public interface AreaService {
    Area saveArea(Area area);
    Area getArea(String name);
    Area getArea(long id);
    List<Area> search(String s);
    List<Area> getAreas();
    void addParkingSpace(String name, ParkingSpace parkingSpace);
}

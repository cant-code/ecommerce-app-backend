package com.mini.ecommerceapp.utils;

import com.mini.ecommerceapp.models.ParkingSpace;

import java.util.Map;

public class Constants {

    private Constants() {}

    public static final String BY_ID = "/byId";
    public static final String BY_NAME = "/byName";
    public static final String AREA = "/area";
    public static final String PARKING_SPACE = "/parkingspace";
    public static final String VEHICLE_SPACE = "/vehicularspace";
    public static final String ORDER = "/orders";
    public static final String PREFERRED_USERNAME = "preferred_username";
    public static final String AREA_NOT_FOUND = "Area Not Found";
    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String PARKING_SPACE_NOT_FOUND = "Parking Space Not Found";
    public static final String VEHICLE_SPACE_NOT_FOUND = "Vehicle Space Not Found";

    public static void calculateSlots(Map<Long, Long> orderMap, ParkingSpace parkingSpace) {
        parkingSpace.getVehicularSpaces().forEach(
                vehicularSpace -> {
                    long total = vehicularSpace.getTotalSlots();
                    long slots = orderMap.containsKey(vehicularSpace.getId()) ?
                            total - orderMap.get(vehicularSpace.getId()) : total;
                    vehicularSpace.setAvailableSlots(slots);
                }
        );
        parkingSpace.getVehicularSpaces().removeIf(vehicularSpace -> vehicularSpace.getAvailableSlots() == 0);
    }
}

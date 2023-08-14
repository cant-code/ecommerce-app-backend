package com.mini.ecommerceapp.utils;

import com.mini.ecommerceapp.models.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.util.List;

public class MockData {

    public static Area buildArea() {
        Area area = new Area();
        area.setName(RandomStringUtils.random(20));
        area.setParkingSlots(List.of(buildParkingSpace()));

        return area;
    }

    public static Order buildOrder() {
        Order order = new Order();

        order.setStatus(Status.CONFIRMED);
        order.setUserId(RandomStringUtils.random(10));

        order.setStart(LocalDateTime.now());
        order.setExpiry(LocalDateTime.now());
        order.setDateCreated(LocalDateTime.now());

        order.setExtraCharges(RandomUtils.nextDouble());
        order.setFinalCharge(RandomUtils.nextDouble());
        order.setTotalCost(RandomUtils.nextDouble());
        order.setItems(buildVehicularSpace());

        return order;
    }

    public static ParkingSpace buildParkingSpace() {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setName(RandomStringUtils.random(20));

        return parkingSpace;
    }

    public static VehicularSpace buildVehicularSpace() {
        VehicularSpace vehicularSpace = new VehicularSpace();

        vehicularSpace.setName(RandomStringUtils.random(20));
        vehicularSpace.setPrice(RandomUtils.nextDouble());
        vehicularSpace.setAvailableSlots(RandomUtils.nextLong());
        vehicularSpace.setTotalSlots(RandomUtils.nextInt());

        return vehicularSpace;
    }
}

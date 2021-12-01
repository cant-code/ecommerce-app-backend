package com.mini.ecommerceapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mini.ecommerceapp.utils.Constants;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Entity
public class VehicularSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotEmpty
    @NotNull
    private String name;
    @NotNull
    private int availableSlots;
    @NotNull
    private int totalSlots;
    @NotNull
    private double price;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parking_space_id", nullable = false)
    private ParkingSpace parkingSpace;

    public VehicularSpace() {}

    public VehicularSpace(@NotEmpty @NotNull String name,
                          @NotNull int availableSlots,
                          @NotEmpty @NotNull int totalSlots,
                          @NotEmpty @NotNull double price,
                          ParkingSpace parkingSpace) {
        this.name = name;
        this.availableSlots = availableSlots;
        this.totalSlots = totalSlots;
        this.price = price;
        this.parkingSpace = parkingSpace;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public long getParkingSpaceID() { return getParkingSpace().getId(); }

    public URI getUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(Constants.VEHICLE_SPACE + "/" + getId())
                .build()
                .toUri();
    }
}

package com.mini.ecommerceapp.models;

import com.mini.ecommerceapp.utils.Constants;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Entity
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;
    @OneToMany(targetEntity = ParkingSpace.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ParkingSpace> parkingSlots;

    public Area() {}

    public Area(@NotNull @NotEmpty String name, List<ParkingSpace> parkingSlots) {
        this.name = name;
        this.parkingSlots = parkingSlots;
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

    public List<ParkingSpace> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(List<ParkingSpace> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    public URI getUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(Constants.AREA + Constants.BY_ID + "/" + getId())
                .build()
                .toUri();
    }
}

package com.mini.ecommerceapp.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mini.ecommerceapp.utils.Constants;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import java.net.URI;
import java.util.List;

@Entity
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "parkingSpace", cascade = CascadeType.ALL)
    private List<VehicularSpace> vehicularSpaces;

    public ParkingSpace() {}

    public ParkingSpace(String name, List<VehicularSpace> fourWheelersSpace) {
        this.name = name;
        this.vehicularSpaces = fourWheelersSpace;
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

    public List<VehicularSpace> getVehicularSpaces() {
        return vehicularSpaces;
    }

    public void setVehicularSpaces(List<VehicularSpace> vehicularSpaces) {
        this.vehicularSpaces = vehicularSpaces;
    }

    public URI getUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(Constants.PARKING_SPACE + Constants.BY_ID + "/" + getId())
                .build()
                .toUri();
    }
}

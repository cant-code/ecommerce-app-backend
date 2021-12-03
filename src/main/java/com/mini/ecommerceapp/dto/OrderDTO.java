package com.mini.ecommerceapp.dto;

import com.mini.ecommerceapp.models.VehicularSpace;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

public class OrderDTO extends BaseTime {
    private VehicularSpace vehicularSpace;

    public OrderDTO() {
    }

    public OrderDTO(@FutureOrPresent LocalDateTime startTimeStamp, @Future LocalDateTime endTimeStamp, VehicularSpace vehicularSpace) {
        super(startTimeStamp, endTimeStamp);
        this.vehicularSpace = vehicularSpace;
    }

    public VehicularSpace getVehicularSpace() {
        return vehicularSpace;
    }

    public void setVehicularSpace(VehicularSpace vehicularSpace) {
        this.vehicularSpace = vehicularSpace;
    }
}

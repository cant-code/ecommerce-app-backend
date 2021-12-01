package com.mini.ecommerceapp.controllers.RequestFormat;

import com.mini.ecommerceapp.models.VehicularSpace;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
public class OrderRequest {
    @Min(value = 1)
    @Max(value = 10)
    private int duration;
    private VehicularSpace vehicularSpace;

    public OrderRequest() {
    }

    public OrderRequest(int duration, VehicularSpace vehicularSpace) {
        this.duration = duration;
        this.vehicularSpace = vehicularSpace;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public VehicularSpace getVehicularSpace() {
        return vehicularSpace;
    }

    public void setVehicularSpace(VehicularSpace vehicularSpace) {
        this.vehicularSpace = vehicularSpace;
    }
}

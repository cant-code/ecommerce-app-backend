package com.mini.ecommerceapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.utils.Constants;
import com.mini.ecommerceapp.utils.QRUtil;
import jakarta.persistence.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userId;
    private LocalDateTime dateCreated;
    private Status status;
    @ManyToOne
    private VehicularSpace items;
    private double totalCost;
    private LocalDateTime start;
    private LocalDateTime expiry;
    private LocalDateTime endTime;
    private double extraCharges;
    private double finalCharge;

    public Order() {}

    public Order(String userId, LocalDateTime dateCreated, Status status, VehicularSpace items, double totalCost, LocalDateTime expiry, LocalDateTime start) {
        this.userId = userId;
        this.dateCreated = dateCreated;
        this.status = status;
        this.items = items;
        this.totalCost = totalCost;
        this.expiry = expiry;
        this.start = start;
    }

    public Order(String userId, OrderDTO request) {
        this.dateCreated = LocalDateTime.now();
        this.status = Status.CONFIRMED;
        this.userId = userId;
        this.items = request.getVehicularSpace();
        this.totalCost = (ChronoUnit.MINUTES.between(request.getStartTimeStamp(), request.getEndTimeStamp())) * request.getVehicularSpace().getPrice() / 60;
        this.expiry = request.getEndTimeStamp();
        this.start = request.getStartTimeStamp();
        this.setExtraCharges(0);
        this.setFinalCharge(this.totalCost);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public VehicularSpace getItems() {
        return items;
    }

    public void setItems(VehicularSpace items) {
        this.items = items;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(double extraCharges) {
        this.extraCharges = extraCharges;
    }

    public double getFinalCharge() {
        return finalCharge;
    }

    public void setFinalCharge(double finalCharge) {
        this.finalCharge = finalCharge;
    }

    public String getDuration() {
        LocalDateTime temp = start;
        long hours = ChronoUnit.HOURS.between(temp, expiry);
        temp = temp.plusHours(hours);
        long minutes = ChronoUnit.MINUTES.between(temp, expiry);
        return hours + ":" + String.format("%02d", minutes);
    }

    public String getQRCode() {
        return QRUtil.generateQR(this);
    }

    public URI getUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(Constants.ORDER + "/" + getId())
                .build()
                .toUri();
    }
}

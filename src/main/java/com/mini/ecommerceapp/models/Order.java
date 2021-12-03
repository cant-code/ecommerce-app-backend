package com.mini.ecommerceapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.utils.Constants;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
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
    @ManyToOne
    private ClientUser user;
    private LocalDateTime dateCreated;
    private Status status;
    @ManyToOne
    private VehicularSpace items;
    private double totalCost;
    private LocalDateTime start;
    private LocalDateTime expiry;

    public Order() {}

    public Order(ClientUser user, LocalDateTime dateCreated, Status status, VehicularSpace items, double totalCost, LocalDateTime expiry, LocalDateTime start) {
        this.user = user;
        this.dateCreated = dateCreated;
        this.status = status;
        this.items = items;
        this.totalCost = totalCost;
        this.expiry = expiry;
        this.start = start;
    }

    public Order(ClientUser user, OrderDTO request) {
        this.dateCreated = LocalDateTime.now();
        this.status = Status.CONFIRMED;
        this.user = user;
        this.items = request.getVehicularSpace();
        this.totalCost = (ChronoUnit.HOURS.between(request.getStartTimeStamp(), request.getEndTimeStamp())) * request.getVehicularSpace().getPrice();
        this.expiry = request.getEndTimeStamp();
        this.start = request.getStartTimeStamp();
    }

    public long getId() {
        return id;
    }

    public Order setId(long id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public ClientUser getUser() {
        return user;
    }

    public Order setUser(ClientUser user) {
        this.user = user;
        return this;
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

    public long getDuration() {
        long duration = ChronoUnit.MINUTES.between(dateCreated, expiry);
        if (duration != 0) return ChronoUnit.HOURS.between(dateCreated, expiry) + 1;
        return ChronoUnit.HOURS.between(dateCreated, expiry);
    }

    public URI getUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(Constants.ORDER + "/" + getId())
                .build()
                .toUri();
    }
}

package com.mini.ecommerceapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mini.ecommerceapp.controllers.RequestFormat.OrderRequest;
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
    private String status;
    @ManyToOne
    private VehicularSpace items;
    private double totalCost;
    private LocalDateTime expiry;

    public Order() {}

    public Order(ClientUser user, LocalDateTime dateCreated, String status, VehicularSpace items, double totalCost, LocalDateTime expiry) {
        this.user = user;
        this.dateCreated = dateCreated;
        this.status = status;
        this.items = items;
        this.totalCost = totalCost;
        this.expiry = expiry;
    }

    public Order(ClientUser user, OrderRequest request) {
        this.dateCreated = LocalDateTime.now();
        this.status = "Confirmed";
        this.user = user;
        this.items = request.getVehicularSpace();
        this.totalCost = request.getDuration() * request.getVehicularSpace().getPrice();
        this.expiry = this.dateCreated.plusHours(request.getDuration()).truncatedTo(ChronoUnit.HOURS);
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

    public Order setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Order setStatus(String status) {
        this.status = status;
        return this;
    }

    public VehicularSpace getItems() {
        return items;
    }

    public Order setItems(VehicularSpace items) {
        this.items = items;
        return this;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Order setTotalCost(double totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public Order setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
        return this;
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

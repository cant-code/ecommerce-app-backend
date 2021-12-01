package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> getByName(String name);
    List<Area> findByNameContainingIgnoreCase(String s);
}

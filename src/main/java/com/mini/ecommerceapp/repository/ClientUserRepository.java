package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    Optional<ClientUser> findByUsername(String name);
    boolean existsByUsername(String name);
}

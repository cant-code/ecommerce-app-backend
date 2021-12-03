package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndExpiry(Status s, LocalDateTime dateTime);
    List<Order> findByUser_Username(String username);
    int countByStartLessThanEqualAndExpiryGreaterThan(LocalDateTime dateTime, LocalDateTime dateTime2);

    @Query("SELECT items.id, COUNT(items.id) FROM Order WHERE start <= ?1 AND ?1 < expiry GROUP BY items.id")
    List<Object[]> countOrders(LocalDateTime dateTime);

    default Map<Long, Long> countOrdersMap(LocalDateTime dateTime) {
        return countOrders(dateTime)
                .stream()
                .collect(Collectors.toMap((arr -> (Long) arr[0]), (arr -> (Long) arr[1])));
    }
}

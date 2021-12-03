package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
    void updateStatus(long id, Status status);

    @Query("SELECT items.id, COUNT(items.id) FROM Order WHERE start <= ?1 AND status = ?2 GROUP BY items.id")
    List<Object[]> countOrders(LocalDateTime dateTime, Status status);

    default Map<Long, Long> countOrdersMap(LocalDateTime dateTime) {
        return countOrders(dateTime, Status.CONFIRMED)
                .stream()
                .collect(Collectors.toMap((arr -> (Long) arr[0]), (arr -> (Long) arr[1])));
    }
}

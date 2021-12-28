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
    List<Order> findByUserId(String username);
    long countOrdersByItems_IdAndStartLessThanEqualAndStartLessThanAndStatus(long id, LocalDateTime t1, LocalDateTime t2, Status status);

    @Modifying
    @Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
    void updateStatus(long id, Status status);

    @Query("SELECT items.id, COUNT(items.id) " +
            "FROM Order WHERE (start <= ?1 OR start < ?2) AND status = ?3 " +
            "GROUP BY items.id")
    List<Object[]> countOrders(LocalDateTime dateTime, LocalDateTime time, Status status);

    @Query("SELECT items.id, COUNT(items.id) " +
            "FROM Order WHERE (start <= ?1 OR start < ?2) AND status = ?3 AND items.parkingSpace.id = ?4 " +
            "GROUP BY items.id")
    List<Object[]> countOrdersForParkingSpace(LocalDateTime dateTime, LocalDateTime time, Status status, long id);

    default Map<Long, Long> countOrdersMap(LocalDateTime startTime, LocalDateTime endTime, long id) {
        List<Object[]> list = null;

        if (id == -1) {
            list = countOrders(startTime, endTime, Status.CONFIRMED);
        } else {
            list = countOrdersForParkingSpace(startTime, endTime, Status.CONFIRMED, id);
        }

        return list
                .stream()
                .collect(Collectors.toMap((arr -> (Long) arr[0]), (arr -> (Long) arr[1])));
    }
}

package com.bookstoremanagement.orders.domain;

import com.bookstoremanagement.orders.domain.models.OrderStatus;
import com.bookstoremanagement.orders.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus orderStatus);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        OrderEntity order = this.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(orderStatus);
        this.save(order);
    }

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query(
            """
            SELECT new com.bookstoremanagement.orders.domain.models.OrderSummary(
                o.orderNumber,
                o.status
            )
            FROM OrderEntity o
            WHERE o.userName = :userName
            """)
    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
            SELECT distinct o
            FROM OrderEntity o left join fetch o.items
            WHERE o.userName = :userName and o.orderNumber = :orderNumber
            """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}

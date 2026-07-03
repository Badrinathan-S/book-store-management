package com.bookstoremanagement.orders.domain;


import com.bookstoremanagement.orders.domain.models.Address;
import com.bookstoremanagement.orders.domain.models.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_seq")
    private Long id;

    @Column(nullable = false)
    private String orderNumber;

    @Column(name = "username", nullable = false)
    private String userName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderItemEntity> items;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "name", column = @Column(name = "customer_name")),
                    @AttributeOverride(name = "email", column = @Column(name = "customer_email")),
                    @AttributeOverride(name = "phone", column = @Column(name = "customer_phone"))
            })
    private Customer customer;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "addressLine1", column = @Column(name = "delivery address line1")),
                    @AttributeOverride(name = "addressLine2", column = @Column(name = "delivery address line2")),
                    @AttributeOverride(name = "city", column = @Column(name = "delivery address city")),
                    @AttributeOverride(name = "state", column = @Column(name = "delivery address state")),
                    @AttributeOverride(name = "zipCode", column = @Column(name = "delivery address zip code")),
                    @AttributeOverride(name = "country", column = @Column(name = "delivery address country"))
            })
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "update_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }


}

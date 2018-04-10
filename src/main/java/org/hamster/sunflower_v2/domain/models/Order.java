package org.hamster.sunflower_v2.domain.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(generator = "order_sequence")
    @GenericGenerator(
            name = "order_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "transaction_order", cascade = CascadeType.ALL)
    private Transaction transaction;

    @CreationTimestamp
    @Column(name = "created_date")
    private java.sql.Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private java.sql.Timestamp modifiedDate;

    public Order() {
    }

    public Order(User buyer, String transactionStatus) {
        this.transactionStatus = transactionStatus;
        this.buyer = buyer;
    }

    public Long getId() {
        return id;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}

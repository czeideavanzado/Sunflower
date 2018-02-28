package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_ordered")
    private Date dateOrdered;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "order")
    private OrderDetails orderDetails;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }
}

package org.hamster.sunflower_v2.domain.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(generator = "transaction_sequence")
    @GenericGenerator(
            name = "transaction_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TRANSACTION_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order transaction_order;

    @CreationTimestamp
    @Column(name = "created_date")
    private java.sql.Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private java.sql.Timestamp modifiedDate;

    public Long getId() {
        return id;
    }

    public Order getTransactionOrder() {
        return transaction_order;
    }

    public void setTransaction_order(Order transaction_order) {
        this.transaction_order = transaction_order;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

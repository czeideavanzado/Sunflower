package org.hamster.sunflower_v2.domain.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(generator = "order_details_sequence")
    @GenericGenerator(
            name = "order_details_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_DETAILS_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetail() {
    }
    public OrderDetail(Order order, Product product) {
        this.order = order;
        this.product = product;
        this.quantity = 1;
        this.price = product.getPrice();
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}

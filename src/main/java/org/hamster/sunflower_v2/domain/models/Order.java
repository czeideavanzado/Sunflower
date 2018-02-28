package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;

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

    
}

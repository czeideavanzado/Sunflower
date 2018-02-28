package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Order;
import org.hamster.sunflower_v2.domain.models.Product;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
public interface OrderService {
    Order createOrder(Long id);
}

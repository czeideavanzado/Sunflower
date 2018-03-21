package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Order;
import org.hamster.sunflower_v2.domain.models.OrderDetail;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
public interface OrderService {
    Order saveOrder(Order order);
    OrderDetail createOrderDetail(OrderDetail orderDetail);
}

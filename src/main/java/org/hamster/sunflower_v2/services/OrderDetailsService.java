package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Order;
import org.hamster.sunflower_v2.domain.models.OrderDetails;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
public interface OrderDetailsService {
    OrderDetails createOrderDetails(Long productId, Long userId);
}

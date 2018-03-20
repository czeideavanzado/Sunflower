package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.OrderDetail;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
public interface OrderDetailService {
    OrderDetail createOrderDetails(Long productId, Long userId);
}

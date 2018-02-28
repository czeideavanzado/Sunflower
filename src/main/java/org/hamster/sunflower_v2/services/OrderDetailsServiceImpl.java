package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Order;
import org.hamster.sunflower_v2.domain.models.OrderDetails;
import org.hamster.sunflower_v2.domain.models.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
@Service(value = "orderDetailsService")
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private ProductService productService;
    private OrderService orderService;
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public OrderDetailsServiceImpl(ProductService productService, OrderService orderService, OrderDetailsRepository orderDetailsRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public OrderDetails createOrderDetails(Long productId, Long userId) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setPrice(productService.findPrice(productId));
        orderDetails.setProduct(productService.find(productId));
        orderDetails.setOrder(orderService.createOrder(userId));
        orderDetails.setQuantity(1);

        return orderDetailsRepository.save(orderDetails);
    }
}

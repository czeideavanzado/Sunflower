package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.OrderDetail;
import org.hamster.sunflower_v2.domain.models.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
@Service(value = "orderDetailService")
public class OrderDetailServiceImpl implements OrderDetailService {

    private ProductService productService;
    private OrderService orderService;
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(ProductService productService, OrderService orderService, OrderDetailRepository orderDetailRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public OrderDetail createOrderDetails(Long productId, Long userId) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPrice(productService.findPrice(productId));
        orderDetail.setProduct(productService.find(productId));
        orderDetail.setOrder(orderService.createOrder(userId));
        orderDetail.setQuantity(1);

        return orderDetailRepository.save(orderDetail);
    }
}

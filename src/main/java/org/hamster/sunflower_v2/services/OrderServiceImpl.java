package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public void cancelTransaction(Long id) {
        Order order = orderRepository.findOne(id);
        order.setTransactionStatus("CANCELLED");
        orderRepository.save(order);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<OrderDetail> findAllDetails() {
        return new ArrayList<>(orderDetailRepository.findAll());
    }

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }
}

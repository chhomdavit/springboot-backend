package com.davit.springblog.service.Impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.davit.springblog.dto.OrderDto;
import com.davit.springblog.entity.Cart;
import com.davit.springblog.entity.Order;
import com.davit.springblog.entity.OrderItem;
import com.davit.springblog.execption.ResourceNotFoundException;
import com.davit.springblog.repository.CartRepository;
import com.davit.springblog.repository.OrderRepository;
import com.davit.springblog.service.OrderItemService;
import com.davit.springblog.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemService orderItemService;
	private final CartRepository cartRepository;
	private final ModelMapper modelMapper;

	@Override
	public OrderDto createOrder(OrderDto orderDto, Integer userId) throws IOException {
		List<Cart> userCarts = cartRepository.findByUsersId(userId);

		if (userCarts.isEmpty()) {
			throw new ResourceNotFoundException("No items in the cart to create an order");
		}

		Order order = new Order();
		order.setUsers(userCarts.get(0).getUsers());
		order.setOrderDate(LocalDateTime.now());
		order.setDiscount(orderDto.getDiscount());
		order.setTax(orderDto.getTax());

		List<OrderItem> orderItems = userCarts.stream().map(cart -> {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setPost(cart.getPost());
			orderItem.setUsers(cart.getUsers());
			orderItem.setQuantityOrder(cart.getQuantityCart());
			orderItem.setTotalPrice(cart.getTotalPrice());
			return orderItem;
		}).collect(Collectors.toList());

		order.setOrderItems(orderItems);

		double subTotal = orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
		order.setSubTotal(subTotal);

		double discountAmount = subTotal * (order.getDiscount() / 100);
		double taxableAmount = subTotal - discountAmount;
		double taxAmount = taxableAmount * (order.getTax() / 100);
		order.setBill(taxableAmount + taxAmount);

		Order savedOrder = orderRepository.save(order);

		for (OrderItem orderItem : orderItems) {
			try {
				orderItemService.createOrderItem(orderItem);
			} catch (IOException e) {
				System.err.println("Error creating order item: " + e.getMessage());
			}
		}

		cartRepository.deleteAll(userCarts);

		return modelMapper.map(savedOrder, OrderDto.class);
	}

}

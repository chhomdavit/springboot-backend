package com.davit.springblog.service;

import java.io.IOException;

import com.davit.springblog.dto.OrderItemDto;
import com.davit.springblog.entity.OrderItem;

public interface OrderItemService {

	OrderItemDto createOrderItem(OrderItem orderItem) throws IOException;
}

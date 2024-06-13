package com.davit.springblog.service.Impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.davit.springblog.dto.OrderItemDto;
import com.davit.springblog.entity.OrderItem;
import com.davit.springblog.repository.OrderItemRepository;
import com.davit.springblog.service.OrderItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

	private final OrderItemRepository orderItemRepository;
	private final ModelMapper modelMapper;

	@Override
	public OrderItemDto createOrderItem(OrderItem orderItem) throws IOException{
		OrderItem savedOrderItem = orderItemRepository.save(orderItem);
		return modelMapper.map(savedOrderItem, OrderItemDto.class);
	}

}

package com.zosh.service;

import java.util.Date;
import java.util.List;

import com.zosh.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.Notification;
import com.zosh.model.Order;
import com.zosh.model.Restaurant;
import com.zosh.repository.NotificationRepository;

@Service
public class NotificationServiceImplementation implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Override
	public Notification sendOrderStatusNotification(Order order) {
		Notification notification = new Notification();
		notification.setMessage("your order is "+order.getOrderStatus()+ " order id is - "+order.getId());
		notification.setCustomer(order.getCustomer());
		notification.setSentAt(new Date());
		
		return notificationRepository.save(notification);
	}

	@Override
	public void sendRestaurantNotification(Restaurant restaurant, String message) {
		
	}

	@Override
	public void sendPromotionalNotification(Users users, String message) {
		
	}

	@Override
	public List<Notification> findUsersNotification(Long userId) {
		return notificationRepository.findByCustomerId(userId);
	}

}

package com.meila.rabbitmq;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

@Service
public class OrderConsumerService {

    private final OrderRepository orderRepository;
    private final JavaMailSender mailSender; // langsung inject JavaMailSender

    public OrderConsumerService(OrderRepository orderRepository, JavaMailSender mailSender) {
        this.orderRepository = orderRepository;
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void receiveOrder(@Payload Order order) {
        try {
            System.out.println("Order received from RabbitMQ: " + order);

            // Update status order jadi PROCESSING
            order.setStatus(Order.OrderStatus.PROCESSING);
            orderRepository.save(order);

            // Proses order + kirim email
            processOrder(order);

            // Update status order jadi COMPLETED
            order.setStatus(Order.OrderStatus.COMPLETED);
            order.setProcessedAt(java.time.LocalDateTime.now());
            orderRepository.save(order);

            System.out.println("Order processed successfully: " + order.getId());

        } catch (Exception e) {
            System.err.println("Error processing order: " + order.getId() + ", Error: " + e.getMessage());

            // Update status jadi FAILED
            order.setStatus(Order.OrderStatus.FAILED);
            orderRepository.save(order);

            throw new RuntimeException("Failed to process order", e);
        }
    }

    private void processOrder(Order order) {
        System.out.println("Processing order: " + order.getId());

        String subject = "Konfirmasi Pesanan: " + order.getProductName();
        String body = "Halo,\n\nPesanan kamu sudah diproses:\n\n" +
                "Produk: " + order.getProductName() + "\n" +
                "Jumlah: " + order.getQuantity() + "\n" +
                "Harga: " + order.getPrice() + "\n\n" +
                "Terima kasih sudah berbelanja.";

        // langsung kirim email pakai JavaMailSender
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getCustomerEmail());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        System.out.println("Email terkirim ke: " + order.getCustomerEmail());
        System.out.println("Order processing completed: " + order.getId());
    }
}

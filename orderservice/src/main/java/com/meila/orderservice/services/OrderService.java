package com.meila.orderservice.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meila.orderservice.model.Order;
import com.meila.orderservice.repository.OrderRepository;
import com.meila.orderservice.vo.Pelanggan;
import com.meila.orderservice.vo.Produk;
import com.meila.orderservice.vo.ResponseTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate; // bukan OrderRepository

    @Autowired
    private DiscoveryClient discoveryClient;

    public OrderService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    // Ambil semua order
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Ambil order berdasarkan ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Mengambil Order beserta Produk dan Pelanggan
   public List<ResponseTemplate> getOrderWithProdukById(Long id){
        List<ResponseTemplate> responseList = new ArrayList<>();
        Order order = getOrderById(id);
        ServiceInstance serviceInstance = discoveryClient.getInstances("PRODUK").get(0);
        Produk produk = restTemplate.getForObject(serviceInstance.getUri() + "/api/produk/"
                + order.getProdukId(), Produk.class);
                serviceInstance = discoveryClient.getInstances("PELANGGAN").get(0);
        Pelanggan pelanggan = restTemplate.getForObject(serviceInstance.getUri() + "/api/pelanggan/"
                + order.getPelangganId(), Pelanggan.class);
        ResponseTemplate vo = new ResponseTemplate();
        vo.setOrder(order);
        vo.setProduk(produk);
        vo.setPelanggan(pelanggan);
        responseList.add(vo);
        return responseList;
    }

    // Membuat order baru
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    // Menghapus order berdasarkan ID
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

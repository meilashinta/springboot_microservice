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
    private DiscoveryClient discoveryClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate; // bukan OrderRepository

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
    public List<ResponseTemplate> getOrderWithProdukById(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();

        Order order = getOrderById(id);
        if (order == null) {
            return responseList; // kosong jika order tidak ditemukan
        }
        ServiceInstance serviceInstancePelanggan = discoveryClient.getInstances("PELANGGANSERVICE").get(0);
        ServiceInstance serviceInstanceProduk = discoveryClient.getInstances("PRODUCTSERVICE").get(0);
        Produk produk = restTemplate.getForObject(
                serviceInstanceProduk.getUri() + "/api/produk/" + order.getProdukId(),
                Produk.class);

        Pelanggan pelanggan = restTemplate.getForObject(
                serviceInstancePelanggan.getUri() + "/api/pelanggan/" + order.getPelangganId(),
                Pelanggan.class);

        // Buat ResponseTemplate langsung pakai constructor
        ResponseTemplate vo = new ResponseTemplate(order, produk, pelanggan);
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

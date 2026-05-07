package com.example.API_Aggregator.client;

import com.example.API_Aggregator.entity.OrderEntity;
import com.example.API_Aggregator.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderClient implements ApiClient {

    private static final Logger log = LoggerFactory.getLogger(OrderClient.class);

    private final OrderRepository orderRepository;
    private final WebClient webClient = WebClient.create();

    public OrderClient(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String getKey() {
        return "orders";
    }

    @Override
    public Mono<Map<String, Object>> fetch(String userId, String requestId) {

        Integer uid = Integer.parseInt(userId);
        List<OrderEntity> dbOrders = orderRepository.findByUserId(uid);

        if (!dbOrders.isEmpty()) {

            log.info("[{}] Orders fetched from DB for user {}", requestId, userId);

            List<Map<String, Object>> ordersList = dbOrders.stream().map(o -> {
                Map<String, Object> m = new HashMap<>();
                m.put("title", o.getTitle());
                m.put("price", o.getPrice());
                m.put("quantity", o.getQuantity());
                return m;
            }).collect(Collectors.toList());

            Map<String, Object> res = new HashMap<>();
            res.put("orders", ordersList);
            res.put("totalOrders", ordersList.size());

            return Mono.just(res);
        }
        log.info("[{}] No orders in DB → calling STABLE API", requestId);

        return webClient.get()
                .uri("https://jsonplaceholder.typicode.com/posts?userId=" + userId)
                .retrieve()
                .bodyToMono(List.class)
                .map(posts -> {

                    Map<String, Object> res = new HashMap<>();

                    if (posts == null || posts.isEmpty()) {

                        log.warn("[{}] No orders from API → NOT SAVING", requestId);

                        res.put("orders", Collections.emptyList());
                        res.put("totalOrders", 0);
                        return res;
                    }

                    List<Map<String, Object>> ordersList = new ArrayList<>();

                    for (Object obj : posts) {

                        Map<String, Object> post = (Map<String, Object>) obj;

                        OrderEntity entity = new OrderEntity();
                        entity.setUserId(uid);
                        entity.setTitle(post.get("title").toString());
                        entity.setPrice(100.0);
                        entity.setQuantity(1);

                        orderRepository.save(entity);

                        Map<String, Object> m = new HashMap<>();
                        m.put("title", entity.getTitle());
                        m.put("price", entity.getPrice());
                        m.put("quantity", entity.getQuantity());

                        ordersList.add(m);
                    }

                    res.put("orders", ordersList);
                    res.put("totalOrders", ordersList.size());

                    log.info("[{}] Orders saved from STABLE API", requestId);

                    return res;
                })
                .onErrorResume(ex -> {

                    log.error("[{}] Orders API error: {}", requestId, ex.getMessage());

                    Map<String, Object> res = new HashMap<>();
                    res.put("orders", Collections.emptyList());
                    res.put("totalOrders", 0);

                    return Mono.just(res);
                });
    }
}
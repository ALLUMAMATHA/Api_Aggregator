package com.example.API_Aggregator.service;

import com.example.API_Aggregator.client.ApiClient;
import com.example.API_Aggregator.dto.DashboardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final ApiClientRegistry registry;

    public DashboardService(ApiClientRegistry registry) {
        this.registry = registry;
    }

    public Mono<DashboardResponse> getDashboardData(String userId, String include) {

        String requestId = UUID.randomUUID().toString();

        Map<String, ApiClient> clients = registry.getClientMap();

        List<String> includes = (include == null || include.isBlank())
                ? Collections.emptyList()
                : Arrays.stream(include.split(","))
                  .map(String::trim)
                  .map(String::toLowerCase)
                  .toList();

        Mono<Map<String, Object>> userMono =
                clients.get("user").fetch(userId, requestId);

        Mono<Map<String, Object>> ordersMono =
                (includes.contains("orders") || includes.contains("payments"))
                        ? clients.get("orders").fetch(userId, requestId)
                        : Mono.just(Collections.emptyMap());

        Mono<Map<String, Object>> paymentsMono =
                includes.contains("payments")
                        ? clients.get("payments").fetch(userId, requestId)
                        : Mono.just(Collections.emptyMap());

        return Mono.zip(userMono, ordersMono, paymentsMono)
                .map(tuple -> {

                    Map<String, Object> responseData = new LinkedHashMap<>();

                    Map<String, Object> user = tuple.getT1();
                    Map<String, Object> orders = tuple.getT2();
                    Map<String, Object> payments = tuple.getT3();


                    boolean userExists = user != null
                            && user.get("name") != null
                            && !"Unavailable".equals(user.get("name"));


                    if (!userExists) {

                        responseData.put("user", Map.of(
                                "id", userId,
                                "name", "User not found"
                        ));

                        responseData.put("orders", Collections.emptyList());

                        responseData.put("payments", Map.of(
                                "status", "NOT_AVAILABLE",
                                "message", "No user → no data"
                        ));

                        log.info("[{}] User not found for userId {}", requestId, userId);

                        return new DashboardResponse(
                                "USER_NOT_FOUND",
                                "User does not exist",
                                responseData
                        );
                    }
                    responseData.put("user", user);

                    if (includes.contains("orders")) {
                        responseData.put("orders", orders);
                    }


                    List<?> ordersList = null;

                    if (orders.get("orders") instanceof List<?>) {
                        ordersList = (List<?>) orders.get("orders");
                    }

                    boolean hasOrders = ordersList != null && !ordersList.isEmpty();

                    String finalStatus;
                    String message;

                    if (includes.contains("payments")) {

                        if (!hasOrders) {

                            responseData.put("payments", Map.of(
                                    "status", "NO_PAYMENT",
                                    "message", "No orders available"
                            ));

                            finalStatus = "NO_PAYMENT";
                            message = "No orders → no payment";

                        } else {

                            responseData.put("payments", payments);

                            finalStatus = "PAYMENT_SUCCESSFUL";
                            message = "Payment completed successfully";
                        }

                    } else if (includes.contains("orders")) {

                        if (hasOrders) {
                            finalStatus = "ORDERS_FETCHED_SUCCESSFULLY";
                            message = "Orders fetched successfully";
                        } else {
                            finalStatus = "NO_ORDERS_FOUND";
                            message = "No orders found";
                        }

                    } else {

                        finalStatus = "USER_FETCHED_SUCCESSFULLY";
                        message = "User fetched successfully";
                    }

                    log.info("[{}] Completed userId {} with status {}",
                            requestId, userId, finalStatus);

                    return new DashboardResponse(finalStatus, message, responseData);
                });
    }
}
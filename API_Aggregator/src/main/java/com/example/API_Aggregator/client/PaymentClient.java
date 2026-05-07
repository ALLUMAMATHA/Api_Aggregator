package com.example.API_Aggregator.client;

import com.example.API_Aggregator.entity.PaymentEntity;
import com.example.API_Aggregator.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class PaymentClient implements ApiClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentClient.class);

    private final PaymentRepository paymentRepository;

    public PaymentClient(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public String getKey() {
        return "payments";
    }

    @Override
    public Mono<Map<String, Object>> fetch(String userId, String requestId) {

        try {
            Integer uid = Integer.parseInt(userId);

            List<PaymentEntity> existing = paymentRepository.findByUserId(uid);

            if (!existing.isEmpty()) {

                PaymentEntity p = existing.get(0);

                log.info("[{}] Payment from DB", requestId);

                return Mono.just(Map.of(
                        "status", p.getStatus(),
                        "method", p.getMethod(),
                        "currency", p.getCurrency()
                ));
            }
            PaymentEntity entity = new PaymentEntity();
            entity.setUserId(uid);
            entity.setStatus("SUCCESS");
            entity.setMethod("UPI");
            entity.setCurrency("INR");

            paymentRepository.save(entity);

            log.info("[{}] Payment saved in DB", requestId);

            return Mono.just(Map.of(
                    "status", entity.getStatus(),
                    "method", entity.getMethod(),
                    "currency", entity.getCurrency()
            ));

        } catch (Exception ex) {

            log.error("[{}] Payment error: {}", requestId, ex.getMessage());

            return Mono.just(Map.of(
                    "status", "FAILED"
            ));
        }
    }
}
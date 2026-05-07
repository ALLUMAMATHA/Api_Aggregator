package com.example.API_Aggregator.client;

import com.example.API_Aggregator.entity.UserEntity;
import com.example.API_Aggregator.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserClient implements ApiClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private final WebClient webClient;
    private final UserRepository userRepository;

    public UserClient(WebClient.Builder builder,
                      UserRepository userRepository) {

        this.webClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();

        this.userRepository = userRepository;
    }

    @Override
    public String getKey() {
        return "user";
    }

    @Override
    public Mono<Map<String, Object>> fetch(String userId, String requestId) {

        Integer uid = Integer.parseInt(userId);


        Optional<UserEntity> dbUser = userRepository.findById(uid);

        if (dbUser.isPresent()) {

            UserEntity u = dbUser.get();

            log.info("[{}] User fetched from DB", requestId);

            Map<String, Object> res = new HashMap<>();
            res.put("id", u.getId());
            res.put("name", u.getName());
            res.put("email", u.getEmail());

            return Mono.just(res);
        }
        return webClient.get()
                .uri("/users/" + userId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(user -> {

                    Map<String, Object> res = new HashMap<>();

                    if (user == null || user.get("id") == null) {

                        log.warn("[{}] No user found → NOT SAVING", requestId);

                        res.put("id", userId);
                        res.put("name", "Unavailable");

                        return res;
                    }
                    UserEntity entity = new UserEntity();
                    entity.setId((Integer) user.get("id"));
                    entity.setName((String) user.get("name"));
                    entity.setEmail((String) user.get("email"));

                    userRepository.save(entity);

                    log.info("[{}] User saved in DB", requestId);

                    res.put("id", entity.getId());
                    res.put("name", entity.getName());
                    res.put("email", entity.getEmail());

                    return res;
                })
                .onErrorResume(ex -> {

                    log.error("[{}] User API error: {}", requestId, ex.getMessage());

                    Map<String, Object> res = new HashMap<>();
                    res.put("id", userId);
                    res.put("name", "Unavailable");

                    return Mono.just(res);
                });
    }
}
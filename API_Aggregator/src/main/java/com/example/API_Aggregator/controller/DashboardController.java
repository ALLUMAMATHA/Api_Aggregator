package com.example.API_Aggregator.controller;

import com.example.API_Aggregator.dto.DashboardResponse;
import com.example.API_Aggregator.service.DashboardService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard/{userId}")
    public Mono<DashboardResponse> getDashboard(
            @PathVariable String userId,
            @RequestParam(required = false) String include) {

        if (!userId.matches("\\d+")) {
            throw new RuntimeException("Invalid userId. It should be numeric.");
        }

        return dashboardService.getDashboardData(userId, include);
    }
}
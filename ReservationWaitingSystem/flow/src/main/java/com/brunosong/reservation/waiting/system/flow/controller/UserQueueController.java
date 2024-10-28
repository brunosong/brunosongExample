package com.brunosong.reservation.waiting.system.flow.controller;

import com.brunosong.reservation.waiting.system.flow.dto.AllowUserResponse;
import com.brunosong.reservation.waiting.system.flow.dto.RegisterUserResponse;
import com.brunosong.reservation.waiting.system.flow.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class UserQueueController {

    private final UserQueueService userQueueService;

    @PostMapping("")
    public Mono<RegisterUserResponse> registerUser(@RequestParam(name = "queue", defaultValue = "default") String queue,
                                                   @RequestParam(name = "user_id") Long userId) {
        return userQueueService.registerWaitQueue(queue,userId)
                .map(RegisterUserResponse::new);
    }

    @PostMapping("/allow")
    public Mono<?> allowUser(@RequestParam(name = "queue", defaultValue = "default") String queue,
                                                   @RequestParam(name = "count") Long count) {
        return userQueueService.allowUser(queue,count)
                .map(allowed -> new AllowUserResponse(count, allowed));
    }

}
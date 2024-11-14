package com.yb.controller

import com.yb.service.NotificationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(
    val notificationService: NotificationService
) {

    @GetMapping(value = ["/sns/produce"])
    fun sendNotification(): String {
        notificationService.sendMessage()
        return "success"
    }

}
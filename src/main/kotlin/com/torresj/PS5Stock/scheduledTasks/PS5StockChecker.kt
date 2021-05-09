package com.torresj.PS5Stock.scheduledTasks

import com.torresj.PS5Stock.services.AmazonCheckerService
import com.torresj.PS5Stock.services.TelegramService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PS5StockChecker(
    val amazonCheckerService: AmazonCheckerService,
    val telegramService: TelegramService
) {
    //Log
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 10000)
    fun amazonChecker() {
        val status = amazonCheckerService.ps5Availability()
        if(status.hasChanged) telegramService.sendNotification(status)
    }
}

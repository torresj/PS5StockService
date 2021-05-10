package com.torresj.PS5Stock.scheduledTasks

import com.torresj.PS5Stock.services.AmazonCheckerService
import com.torresj.PS5Stock.services.GameCheckerService
import com.torresj.PS5Stock.services.TelegramService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PS5StockChecker(
    val amazonCheckerService: AmazonCheckerService,
    val gameCheckerService: GameCheckerService,
    val telegramService: TelegramService
) {
    //Log
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 20000)
    fun amazonChecker() {
        try {
            val status = amazonCheckerService.ps5Availability()
            if (status.hasChanged) telegramService.sendNotification(status)
        } catch (e: Exception){
            val errorMessage ="Error checking amazon stock: ${e.localizedMessage}"
            logger.error(errorMessage)
            telegramService.sendErrorNotification(errorMessage)
        }
    }

    fun gameChecker() {
        try {
            val status = gameCheckerService.ps5Availability()
            if (status.hasChanged) telegramService.sendNotification(status)
        } catch (e: Exception){
            val errorMessage ="Error checking game stock: ${e.message}"
            logger.error(errorMessage)
            telegramService.sendErrorNotification(errorMessage)
        }
    }
}

package com.torresj.PS5Stock.scheduledTasks

import com.torresj.PS5Stock.services.*
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PS5StockChecker(
    val amazonCheckerService: AmazonCheckerService,
    val gameCheckerService: GameCheckerService,
    val pcComponentesCheckerService: PCComponentesCheckerService,
    val mediaMarketCheckerService: MediaMarketCheckerService,
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
            val errorMessage ="Error checking Amazon stock: ${e.localizedMessage}"
            logger.error(errorMessage)
            telegramService.sendErrorNotification("An error has occured during Amazon stock checker: $errorMessage")
        }
    }

    @Scheduled(fixedRate = 20000)
    fun gameChecker() {
        try {
            val status = gameCheckerService.ps5Availability()
            if (status.hasChanged) telegramService.sendNotification(status)
        } catch (e: Exception){
            val errorMessage ="Error checking Game stock: ${e.message}"
            logger.error(errorMessage)
            telegramService.sendErrorNotification("An error has occured during Game stock checker: $errorMessage")
        }
    }

    //@Scheduled(fixedRate = 200000)
    fun pccomponentesChecker() {
        try {
            val status = pcComponentesCheckerService.ps5Availability()
            if (status.hasChanged) telegramService.sendNotification(status)
        } catch (e: Exception){
            val errorMessage ="Error checking PcComponentes stock: ${e.message}"
            logger.error(errorMessage)
            telegramService.sendErrorNotification("An error has occured during PCComponentes stock checker: $errorMessage")
        }
    }

    //@Scheduled(fixedRate = 200000)
    fun mediamarketChecker() {
        try {
            val status = mediaMarketCheckerService.ps5Availability()
            if (status.hasChanged) telegramService.sendNotification(status)
        } catch (e: Exception){
            val errorMessage ="Error checking MediaMarket stock: ${e.message}"
            logger.error(errorMessage)
            telegramService.sendErrorNotification("An error has occured during MediaMarket stock checker: $errorMessage")
        }
    }
}

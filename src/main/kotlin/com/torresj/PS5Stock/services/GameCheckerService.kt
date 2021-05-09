package com.torresj.PS5Stock.services


import com.torresj.PS5Stock.dtos.PS5Status
import com.torresj.PS5Stock.dtos.Web
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class GameCheckerService {
    //Log
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${ps5.game.url}")
    lateinit var url: String

    var previousAvailability: String = "Producto no disponible"

    fun ps5Availability(): PS5Status {
        var status: PS5Status
        val webPage = Jsoup
            .connect(url)
            .get()
        val availability = webPage.getElementsByClass("buy--type").first().getElementsByTag("span").text()
        logger.debug("Avialability: $availability")
        status = if (availability.contains("Producto no disponible")) {
            PS5Status(available = false, statusText = availability.trim(), url = url, web = Web.GAME)
        } else {
            PS5Status(available = true, statusText = "Disponible", url = url, web = Web.GAME)
        }

        if (availability != previousAvailability) status.hasChanged = true

        previousAvailability = availability

        logger.debug("Status = $status")
        return status
    }
}

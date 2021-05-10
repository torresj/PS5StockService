package com.torresj.PS5Stock.services


import com.torresj.PS5Stock.dtos.PS5Status
import com.torresj.PS5Stock.dtos.Web
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class MediaMarketCheckerService {
    //Log
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${ps5.mediamarket.url}")
    lateinit var url: String

    var previousAvailability: String = ""

    fun ps5Availability(): PS5Status {
        var status: PS5Status
        val headers = mapOf(
            "accept-language" to "es-ES,es;q=0.8",
        )
        val webPage = Jsoup
            .connect(url)
            .headers(headers)
            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
            .referrer("https://www.mediamarkt.es/es/specials/sony-playstation5")
            .get()
        val availability = if (webPage.toString()
                .contains("Este artículo no está disponible actualmente.")
        ) "Este artículo no está disponible actualmente." else ""
        logger.debug("Avialability: $availability")
        status = if (availability.contains("Este artículo no está disponible actualmente.")) {
            PS5Status(available = false, statusText = availability.trim(), url = url, web = Web.MEDIAMARKET)
        } else {
            PS5Status(available = true, statusText = availability, url = url, web = Web.MEDIAMARKET)
        }

        if (availability != previousAvailability) status.hasChanged = true

        previousAvailability = availability

        logger.debug("Status = $status")
        return status
    }
}

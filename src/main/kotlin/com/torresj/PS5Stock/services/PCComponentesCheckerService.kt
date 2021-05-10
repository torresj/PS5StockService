package com.torresj.PS5Stock.services


import com.torresj.PS5Stock.dtos.PS5Status
import com.torresj.PS5Stock.dtos.Web
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class PCComponentesCheckerService {
    //Log
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${ps5.pccomponentes.url}")
    lateinit var url: String

    var previousAvailability: String = ""

    fun ps5Availability(): PS5Status {
        var status: PS5Status
        val headers = mapOf(
            "authority" to "www.pccomponentes.com",
            "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            "accept-encoding" to "gzip, deflate, br",
            "accept-language" to "es-ES,es;q=0.9",
            "referer" to "https://www.google.com/",
            "cache-control" to "max-age=0",
            "sec-ch-ua-mobile" to "?0",
            "sec-ch-ua" to "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"",
            "sec-fetch-dest" to "document",
            "sec-fetch-mode" to "navigate",
            "sec-fetch-site" to "cross-site",
            "sec-fetch-user" to "?1",
            "upgrade-insecure-requests" to "1",
            "user-agent" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
        )
        val webPage = Jsoup
            .connect(url)
            .headers(headers)
            .get()
        val availability = webPage.getElementById("priceBlock").getElementsByTag("p").text()
        logger.debug("Avialability: $availability")
        status = if (availability.contains("No disponible")) {
            PS5Status(available = false, statusText = availability.trim(), url = url, web = Web.PCCOMPONENTES)
        } else {
            PS5Status(available = true, statusText = availability, url = url, web = Web.PCCOMPONENTES)
        }

        if (availability != previousAvailability) status.hasChanged = true

        previousAvailability = availability

        logger.debug("Status = $status")
        return status
    }
}

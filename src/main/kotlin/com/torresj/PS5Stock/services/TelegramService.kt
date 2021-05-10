package com.torresj.PS5Stock.services

import com.torresj.PS5Stock.dtos.PS5Status
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URL
import java.net.URLEncoder

@Service
class TelegramService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${telegram.auth-token}")
    private lateinit var token: String

    @Value("\${telegram.chat-id}")
    private lateinit var chatId: String

    @Value("\${telegram.error-chat-id}")
    private lateinit var errorChatId: String

    fun sendNotification(status: PS5Status) {
        val urlString =
            "https://api.telegram.org/bot${token}/sendMessage?chat_id=${chatId}&text=${buildMessage(status)}&parse_mode=html"
        URL(urlString).readText()
        logger.info("[TELEGRAM SERVICE] Message sent")
    }

    fun sendErrorNotification(message: String) {
        val urlString =
            "https://api.telegram.org/bot${token}/sendMessage?chat_id=${errorChatId}&text=${message}&parse_mode=html"
        URL(urlString).readText()
        logger.error("[TELEGRAM SERVICE] Error message sent")
    }

    private fun buildMessage(status: PS5Status): String {
        val message = if (!status.available) {
            "&#10060; La PS5 sigue sin estar disponible en <b>${status.web}</b>\n \n&#128308; Stock: <i>${status.statusText}</i>\n \n<a href=\"${status.url}\">&#127918; Consola PlayStation 5 en ${status.web}</a>\n"
        } else {
            "&#127881;&#127882;<b> ¡LA PS5 YA ESTÁ DISPONIBLE EN ${status.web}! &#127881;&#127882;</b>\n \n&#9989; Stock: <i>${status.statusText}</i>\n \n<a href=\"${status.url}\">&#127918; Consola PlayStation 5 en ${status.web}</a>\n"
        }
        return URLEncoder.encode(message, "UTF-8")
    }
}

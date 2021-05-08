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

    fun sendNotification(status: PS5Status) {
        val urlString =
            "https://api.telegram.org/bot${token}/sendMessage?chat_id=${chatId}&text=${buildMessage(status)}&parse_mode=html"
        URL(urlString).readText()
        logger.info("[TELEGRAM SERVICE] Message sent")
    }

    private fun buildMessage(status: PS5Status): String {
        var message = ""
        if(!status.available){
            message = "La PS5 sigue sin estar disponible en <b>${status.web}</b>\nStock: <i>${status.statusText}</i>\n<a href=\"${status.url}\">Consola PlayStation 5 en ${status.web}</a>"
        }else{
            message = "<b>¡LA PS5 YA ESTÁ DISPONIBLE EN ${status.web}!</b>\nStock: <i>${status.statusText}</i>\n<a href=\"${status.url}\">Consola PlayStation 5 en ${status.web}</a>"
        }
        return URLEncoder.encode(message, "UTF-8")
    }
}

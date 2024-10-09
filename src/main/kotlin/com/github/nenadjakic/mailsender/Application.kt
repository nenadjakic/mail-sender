package com.github.nenadjakic.mailsender

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.nenadjakic.mailsender.config.MailSenderProperties
import com.github.nenadjakic.mailsender.dto.MailDto
import com.github.nenadjakic.mailsender.extension.toFile
import com.github.nenadjakic.mailsender.service.MailService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import java.lang.Exception
import java.lang.RuntimeException

@EnableAsync
@SpringBootApplication
class Application(
    private val mailService: MailService,
    private val objectMapper: ObjectMapper
) {
    val logger = LoggerFactory.getLogger(Application::class.java)

    @RabbitListener(queues = ["\${mail-sender.queue-name}"])
    fun handleMessage(message: String) {
        try {
            val mailDto = objectMapper.readValue(message, MailDto::class.java)
            mailService.sendMailSync(
                mailDto.to,
                mailDto.cc,
                mailDto.bcc,
                mailDto.subject,
                mailDto.body,
                mailDto.isHtml
            )
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }
    }

    @RabbitListener(queues = ["\${mail-sender.dead-letter-queue-name}"])
    fun handleDlxMessage(message: String?) {
        logger.error("Failed rabbit mq message: {}.", message)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
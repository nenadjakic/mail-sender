package com.github.nenadjakic.mailsender.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "mail-sender")
class MailSenderProperties {
    lateinit var from: String
    lateinit var queueName: String
    lateinit var deadLetterQueueName: String
}
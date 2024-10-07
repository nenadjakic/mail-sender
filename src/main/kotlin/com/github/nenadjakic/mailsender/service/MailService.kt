package com.github.nenadjakic.mailsender.service

import com.github.nenadjakic.mailsender.extension.toInternetAddress
import jakarta.mail.internet.InternetAddress
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File

@Service
class MailService(
    private val mailSender: JavaMailSender,
    @Value("mail-sender.from") private val from: String
) {

    @Async
    fun sendMail(
        to: Collection<String>,
        cc: Collection<String>?,
        bcc: Collection<String>?,
        subject: String,
        body: String,
        isHtml: Boolean
    ) =
        sendMail(
            to.mapNotNull { it.toInternetAddress() },
            cc?.mapNotNull { it.toInternetAddress() },
            bcc?.mapNotNull { it.toInternetAddress() },
            subject,
            body,
            isHtml,
            null
        )

    @Async
    fun sendMail(
        to: Collection<InternetAddress>,
        cc: Collection<InternetAddress>?,
        bcc: Collection<InternetAddress>?,
        subject: String,
        body: String,
        isHtml: Boolean,
        attachments: Collection<File>?
    ) {
        val message = mailSender.createMimeMessage()
        val isMultipart = isHtml || !attachments.isNullOrEmpty()
        MimeMessageHelper(message, isMultipart).apply {

            if (!from.isNullOrEmpty()) {
                setFrom(from)
            }
            setTo(to.toTypedArray())
            if (!cc.isNullOrEmpty()) {
                setCc(cc.toTypedArray())
            }

            if (!bcc.isNullOrEmpty()) {
                setBcc(bcc.toTypedArray())
            }

            setSubject(subject)
            setText(body, isHtml)

            if (!attachments.isNullOrEmpty()) {
                for (attachment in attachments) {
                    addAttachment(attachment.name, attachment)
                }
            }
        }

        mailSender.send(message)
    }
}
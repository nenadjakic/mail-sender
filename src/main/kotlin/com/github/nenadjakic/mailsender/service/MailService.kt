package com.github.nenadjakic.mailsender.service

import com.github.nenadjakic.mailsender.extension.toInternetAddress
import jakarta.mail.internet.InternetAddress
import org.springframework.beans.factory.annotation.Value
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
    fun sendMailAsync(
        to: Collection<String>,
        cc: Collection<String>?,
        bcc: Collection<String>?,
        subject: String,
        body: String,
        isHtml: Boolean,
        attachments: Collection<File>? = null
    ) = sendMail (
        to.mapNotNull { it.toInternetAddress() },
        cc?.mapNotNull { it.toInternetAddress() },
        bcc?.mapNotNull { it.toInternetAddress() },
        subject,
        body,
        isHtml,
        attachments
    )

    fun sendMailSync (
        to: Collection<String>,
        cc: Collection<String>?,
        bcc: Collection<String>?,
        subject: String,
        body: String,
        isHtml: Boolean,
        attachments: Collection<File>? = null
    ) =
        sendMail (
            to.mapNotNull { it.toInternetAddress() },
            cc?.mapNotNull { it.toInternetAddress() },
            bcc?.mapNotNull { it.toInternetAddress() },
            subject,
            body,
            isHtml,
            attachments
        )

    fun sendMail (
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

            if (from.isNotEmpty()) {
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

            attachments?.forEach { addAttachment(it.name, it) }
        }

        mailSender.send(message)
    }
}
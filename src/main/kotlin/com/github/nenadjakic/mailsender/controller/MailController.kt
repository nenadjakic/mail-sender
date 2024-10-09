package com.github.nenadjakic.mailsender.controller

import com.github.nenadjakic.mailsender.dto.MailDto
import com.github.nenadjakic.mailsender.extension.toFile
import com.github.nenadjakic.mailsender.service.MailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/mail")
class MailController(
    private val mailService: MailService
) {

    @PostMapping("/send")
    fun sendMail(
        @RequestPart("mailDto") mailDto: MailDto,
        @RequestPart(value = "attachments", required = false) attachments: Collection<MultipartFile>?
    ): ResponseEntity<Void> {
        mailService.sendMailAsync(
            mailDto.to,
            mailDto.cc,
            mailDto.bcc,
            mailDto.subject,
            mailDto.body,
            mailDto.isHtml,
            attachments?.map { it.toFile() }
        )

        return ResponseEntity.noContent().build()
    }
}
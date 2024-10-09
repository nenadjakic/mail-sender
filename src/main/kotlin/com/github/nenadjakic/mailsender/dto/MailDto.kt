package com.github.nenadjakic.mailsender.dto

import jakarta.mail.internet.InternetAddress

class MailDto {
    lateinit var to: Collection<String>
    var cc: Collection<String>? = null
    var bcc: Collection<String>? = null
    lateinit var subject: String
    lateinit var body: String
    var isHtml: Boolean = false
}

package com.github.nenadjakic.mailsender.extension

import jakarta.mail.internet.AddressException
import jakarta.mail.internet.InternetAddress

fun String.toInternetAddress(): InternetAddress? =
    try {
        InternetAddress(this).apply {
            validate()
        }
    } catch (e: AddressException) {
        null
    }

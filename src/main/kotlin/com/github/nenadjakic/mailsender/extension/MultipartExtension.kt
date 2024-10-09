package com.github.nenadjakic.mailsender.extension

import org.springframework.web.multipart.MultipartFile
import java.io.File

fun MultipartFile.toFile(): File {
    val tempFile = File.createTempFile("temp-", this.originalFilename)
    this.transferTo(tempFile)
    return tempFile
}
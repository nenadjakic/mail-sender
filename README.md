# mail-sender

## Table of Contents
- [Description](#description)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
- [Usage](#usage)
   - [REST API](#rest-api)
     - [Request body](#request-body)
       - [Example Request](#example-request)
   - [RabbitMQ's Integration](#rabbitmqs-integration)
     - [Example Message](#example-message)
- [Exception Handling](#exception-handling)
- [License](#license)

## Description
This is a Spring Boot application for sending emails via SMTP. It supports both asynchronous and synchronous email
sending, handles attachments, and integrates with RabbitMQ for message queuing.

## Features
- Send emails with optional CC and BCC addresses.
- Send emails with or without attachments.
- Support for both plain text and HTML email bodies.
- Asynchronous email sending for improved performance.
- RabbitMQ's integration for sending emails through message queues.
- Error handling and dead-letter queue support for failed messages.

## Technologies Used
- **Spring Boot** - A framework for building Java applications.
- **Spring Mail** - For sending emails.
- **Spring AMQP** - For message queuing and handling.

## Getting Started

### Prerequisites
- JDK 21 or higher
- RabbitMQ server
- SMTP server for sending emails

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/nenadjakic/mail-sender.git
   cd mail-sender
   ```

2. Configure the application properties:
   Update the `application.properties` with your SMTP server and RabbitMQ configuration.

   ```properties
   mail-sender.from=no-reply@example.com
   mail-sender.queue-name= mail_queue
   mail-sender.dead-letter-queue-name= mail_dead_letter_queue

   spring.mail.host=smtp.example.com
   spring.mail.port= 587
   spring.mail.username= your-email@example.com
   spring.mail.password= your-email-password
   
   spring.rabbitmq.host=http://example.com
   spring.rabbitmq.port=5672
   spring.rabbitmq.username=guest
   spring.rabbitmq.password=guest
   ```

3. Build the project:
   ```
   ./gradlew clean build
   ```

4. Run the project:
   ```
   ./gradlew bootRun
   ```

## Usage

### REST API
You can send emails via the following API endpoint:

- **POST** `/api/mail/send`

#### Request Body
The API accepts multipart form data. The `mailDto` part is a JSON object that contains the email details,
and `attachments` is an optional part for file uploads.

- `to` - List of recipient email addresses.
- `cc` - List of CC email addresses (optional).
- `bcc` - List of BCC email addresses (optional).
- `subject` - Email subject.
- `body` - Email body (HTML or plain text).
- `isHtml` - Boolean indicating if the body is HTML.

##### Example Request:
   ```
   curl -X POST "http://localhost:8080/api/mail/send" \
     -H "Content-Type: multipart/form-data" \
     -F 'mailDto={"to":["recipient@example.com"], "subject":"Test Email", "body":"This is a test email.", "isHtml":false}' \
     -F "attachments=@/path/to/file.txt"
   ```

### RabbitMQ's Integration
Emails can also be sent by publishing a message to RabbitMQ. The service listens for messages on the configured
queue (`mail-sender.queue-name`).

- **Queue**: `${mail-sender.queue-name}`

#### Example Message
The message should be in JSON format with the same structure as the `MailDto` object.

    ```json
    {
      "to": ["recipient@example.com"],
      "cc": null,
      "bcc": null,
      "subject": "Test Email",
      "body": "This is a test email.",
      "isHtml": false
    }
    ```

## Exception Handling
If an error occurs while sending the email, the message is routed to a **dead-letter queue
** (`${mail-sender.dead-letter-queue-name}`), and an error is logged.

## License
This project is licensed under the Apache License - see the LICENSE file for details.

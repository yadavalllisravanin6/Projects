package com.aireviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiCodeReviewBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeReviewBotApplication.class, args);
        System.out.println(" AI Code Review Bot is running!");
        System.out.println("📡 Listening for GitHub webhooks at /api/webhook/github");
    }
}

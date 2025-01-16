package com.harrison;

import com.huatong.teleagi.ai.chat.TeleAgiChatModel;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyAiApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyAiApplication.class, args);
	}
}
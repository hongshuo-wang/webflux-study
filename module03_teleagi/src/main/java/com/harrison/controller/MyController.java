package com.harrison.controller;

import com.huatong.teleagi.ai.chat.TeleAgiChatModel;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangHS
 * @date 2025/1/15 17:36
 */
@RestController
@RequestMapping("/ai/chat")
public class MyController {
	Logger log = LoggerFactory.getLogger(MyController.class);

	@Resource
	private TeleAgiChatModel chatModel;

	@GetMapping("/call")
	public String test() {
		List<Message> chatMessage = new ArrayList<>();
		UserMessage userMessage1 = new UserMessage("打车");
		chatMessage.add(userMessage1);
		AssistantMessage assistantMessage1 = new AssistantMessage("{\"意图\": \"打车\"}");
		chatMessage.add(assistantMessage1);
		UserMessage userMessage2 = new UserMessage("东直门到西直门");
		chatMessage.add(userMessage2);
		Prompt prompt = new Prompt(chatMessage);
		ChatResponse response = chatModel.call(prompt);
		log.info(response.getResult().getOutput().getContent());
		return response.getResult().getOutput().getContent();
	}
}

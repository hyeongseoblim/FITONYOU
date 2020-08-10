package com.web.curation.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.dao.ChatDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.Chat;
import com.web.curation.model.ChatDTO;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	ChatDao chatDao;

	@GetMapping("/existroom")
	public Object existroom(@RequestParam String firstuser, @RequestParam String seconduser) {
		System.out.println(1);
		final BasicResponse result = new BasicResponse();
		Chat chat;
		Optional<ChatDTO> optChatdto = chatDao.getRoomnameByUserInfos(firstuser, seconduser);
		if (optChatdto.isPresent()) {// 있다면 lasttime을 update후에
			System.out.println(2);
			// 룸네임을 반환
			chat = new Chat(optChatdto.get());
			chatDao.updateLasttime(chat.getRoomname());
			result.data = "success";
			result.object = chat;
			result.status = true;
		} else {
			System.out.println(3);
			// 새로운 룸네임을 저장후 이를 반환
			String roomname = firstuser + seconduser;
			chatDao.InsertRoomname(firstuser, seconduser, roomname);
			optChatdto = chatDao.getRoomnameByUserInfos(firstuser, seconduser);
			chat = new Chat(optChatdto.get());
			result.object = chat;
			result.data = "success";
			result.status = true;
		}
		System.out.println(4);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}

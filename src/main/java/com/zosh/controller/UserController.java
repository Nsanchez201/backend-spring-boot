package com.zosh.controller;

import com.zosh.model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.Exception.UserException;
import com.zosh.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/profile")
	public ResponseEntity<Users> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

		Users users = userService.findUserProfileByJwt(jwt);
		users.setPassword(null);

		return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
	}

}

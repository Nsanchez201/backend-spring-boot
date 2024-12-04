package com.zosh.service;

import java.util.List;

import com.zosh.Exception.UserException;
import com.zosh.model.Users;

public interface UserService {

	public Users findUserProfileByJwt(String jwt) throws UserException;
	
	public Users findUserByEmail(String email) throws UserException;

	public List<Users> findAllUsers();

	public List<Users> getPenddingRestaurantOwner();

	void updatePassword(Users users, String newPassword);

	void sendPasswordResetEmail(Users users);

}

package com.zosh.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.zosh.model.Users;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zosh.Exception.UserException;
import com.zosh.config.JwtProvider;
import com.zosh.model.PasswordResetToken;
import com.zosh.repository.PasswordResetTokenRepository;
import com.zosh.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	private UserRepository userRepository;
	private JwtProvider jwtProvider;
	private PasswordEncoder passwordEncoder;
	private PasswordResetTokenRepository passwordResetTokenRepository;
	private JavaMailSender javaMailSender;
	
	public UserServiceImplementation(
			UserRepository userRepository,
			JwtProvider jwtProvider,
			PasswordEncoder passwordEncoder,
			PasswordResetTokenRepository passwordResetTokenRepository,
			JavaMailSender javaMailSender) {
		
		this.userRepository=userRepository;
		this.jwtProvider=jwtProvider;
		this.passwordEncoder=passwordEncoder;
		this.passwordResetTokenRepository=passwordResetTokenRepository;
		this.javaMailSender=javaMailSender;
	}

	@Override
	public Users findUserProfileByJwt(String jwt) throws UserException {
		String email=jwtProvider.getEmailFromJwtToken(jwt);
		
		Users users = userRepository.findByEmail(email);
		
		if(users ==null) {
			throw new UserException("users not exist with email "+email);
		}
		return users;
	}

	@Override
	public List<Users> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<Users> getPenddingRestaurantOwner() {
		return userRepository.getPenddingRestaurantOwners();
	}
	
	@Override
    public void updatePassword(Users users, String newPassword) {
        users.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(users);
    }

	@Override
	public void sendPasswordResetEmail(Users users) {
		
		// Generate a random token (you might want to use a library for this)
        String resetToken = generateRandomToken();
        
        // Calculate expiry date
        Date expiryDate = calculateExpiryDate();

        // Save the token in the database
        PasswordResetToken passwordResetToken = new PasswordResetToken(resetToken, users,expiryDate);
        passwordResetTokenRepository.save(passwordResetToken);

        // Send an email containing the reset link
        sendEmail(users.getEmail(), "Password Reset", "Click the following link to reset your password: http://localhost:3000/account/reset-password?token=" + resetToken);
	}

	private void sendEmail(String to, String subject, String message) {
	    SimpleMailMessage mailMessage = new SimpleMailMessage();

	    mailMessage.setTo(to);
	    mailMessage.setSubject(subject);
	    mailMessage.setText(message);

	    javaMailSender.send(mailMessage);
	}

	private String generateRandomToken() {
	    return UUID.randomUUID().toString();
	}

	private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime();
    }
	
	@Override
	public Users findUserByEmail(String username) throws UserException {
		
		Users users =userRepository.findByEmail(username);
		
		if(users !=null) {
			return users;
		}
		
		throw new UserException("users not exist with username "+username);
	}

}

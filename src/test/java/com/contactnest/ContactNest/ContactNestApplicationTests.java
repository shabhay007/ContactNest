package com.contactnest.ContactNest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.contactnest.ContactNest.Services.EmailService;

@SpringBootTest
class ContactNestApplicationTests {

	@Test
	void contextLoads() {
	}





	// testing mail sender

	@Autowired
	private EmailService emailService;

	@Test
	void sendEmailTest(){
		emailService.sendEmail(
			"shabhay007@gmail.com", 
			"For Testing purpose.", 
			"Testing email service"
		);
	}

}

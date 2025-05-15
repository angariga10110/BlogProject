package blog_project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog_project.com.services.AccountService;

@Controller
public class AccountRegisterController {
	@Autowired
	private AccountService accountService;

	// 登録画面の表示
	@GetMapping("/register")
	public String getAccountRegisterPage() {
		return "register.html";
	}

	// 登録処理をする
	@PostMapping("/account/register/process")
	public String accountRegisterProcess(@RequestParam String accountName, @RequestParam String accountEmail,
			@RequestParam String password) {
		// もし、createAccountがtrue login.html
		// そうではない場合 register.htmlにとどまります
		if (accountService.createAccount(accountName, accountEmail, password)) {
			return "login.html";
		} else {
			return "register.html";
		}
	}

}

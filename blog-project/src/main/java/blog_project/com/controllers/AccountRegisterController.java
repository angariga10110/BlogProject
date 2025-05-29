package blog_project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String getAccountRegisterPage(Model model) {
		if (!model.containsAttribute("role")) {
			model.addAttribute("role", "USER");
		}
		return "register.html";
	}

	// 登録処理をする
	@PostMapping("/account/register/process")
	public String accountRegisterProcess(@RequestParam String accountName, @RequestParam String accountEmail,
			@RequestParam String password, @RequestParam String confirmPassword,
			@RequestParam(defaultValue = "USER") String role, Model model) {
		// パスワード不一致チェック（サーバー側でも再確認）
		if (!password.equals(confirmPassword)) {
			model.addAttribute("errorMessage", "パスワードが一致しません。再度ご確認ください。");
			// 入力済みユーザー名・メールアドレスを保持
			model.addAttribute("accountName", accountName);
			model.addAttribute("accountEmail", accountEmail);
			model.addAttribute("role", role);
			return "register.html";
		}

		// アカウント作成
		boolean ok = accountService.createAccount(accountName, accountEmail, password, role);
		if (!ok) {
			model.addAttribute("errorMessage", "登録に失敗しました。別のメールアドレスをお試しください。");
			model.addAttribute("accountName", accountName);
			model.addAttribute("accountEmail", accountEmail);
			model.addAttribute("role", role);
			return "register.html";
		}
		return "redirect:/login";
	}
}

package blog_project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog_project.com.models.entity.Account;
import blog_project.com.services.AccountService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountLoginController {

	@Autowired
	private AccountService accountService;
	// Sessionが使えるように宣言
	@Autowired
	private HttpSession session;

	// ログイン画面の表示
	@GetMapping("/login")
	public String getAccountLoginPage(@RequestParam(name = "error", required = false) String error, Model model) {
		if (error != null) {
			// error パラメータがあればエラーメッセージをセット
			model.addAttribute("errorMessage",
	                "ログインに失敗しました。EmailかPasswordご確認ください。");
	        }return "login";
	}

	// ログイン処理をする
	@PostMapping("/account/login/process")
	public String accountLoginProcess(@RequestParam String accountEmail, @RequestParam String password, Model model) {
		// loginCheckメソッドを呼び出してそのけっかをaccountという変数に格納
		Account account = accountService.loginCheck(accountEmail, password);
		// もし、account=nullログイン画面とどまります
		// そうでない場合、Sessionにログイン情報に保存
		// 商品一覧画面にリダイレクトする/home
		if (account == null) {
			return "redirect:/login?error";
		} else {
			session.setAttribute("loginAccountInfo", account);
			if ("ADMIN".equals(account.getRole())) {
		       
		        return "redirect:/home";
		    } else {
		      
		        return "redirect:/dashboard";
		    }
		}
	}
}

package blog_project.com.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog_project.com.models.entity.Account;
import blog_project.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogRegiterController {
	@Autowired
	private BlogService blogService;

	@Autowired
	private HttpSession session;

	// 登録画面の表示
	@GetMapping("/blog/register")
	public String getBlogRegisterPage(Model model) {
		// セッションからログインしている人の情報をaccountという変数に格納
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし、account==nullログイン画面にリダイレクトする
		// そうっではない場合
		// ログインしている人の名前を画面に渡す
		// 商品登録のhtmlを表示させる
		if (account == null) {
			return "redirect:login.html";
		} else {
			model.addAttribute("accountName", account.getAccountName());
			return "blog-register.html";
		}
	}

	// 商品の登録処理
	@PostMapping("/blog/register/process")
	public String BlogregisterProcess(@RequestParam String accountName, @RequestParam String accountEmail,
			@RequestParam String password) {
		// セッションからログインしている人の情報をadminという変数に格納
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし、account==nullログイン画面にリダイレクトする
		// そうっではない場合は、画像のファイル名取得する
		// 画像のアップロード
		// もし同じファイルの名前がなかったら保存
		// 商品一覧画面にリダイレクトする
		// そうではない場合、商品登録画面にとどまります
		if(account == null) {
			return "redirect:/login";
		}else {
			// ファイルの名前取得する
			String fileName = new SimpleDateFormat("yyyy-MM--dd-HH-mm-ss-").format(new Date())
					+blogImage.getOriginalFilename();
			// ファイルの保存
			try {
				Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + fileName));
			}
		}
	}

}

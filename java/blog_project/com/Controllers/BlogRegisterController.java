package blog_project.com.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import blog_project.com.models.entity.Account;
import blog_project.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogRegisterController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private HttpSession session;

	/**
	 * 新規投稿画面の表示メソッド
	 */
	@GetMapping("/blog/register")
	public String showRegisterForm(Model model) {
		// セッションからログイン中ユーザー情報を取得
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			// 未ログインならログイン画面へリダイレクト
			return "redirect:/login";
		}
		// ログインユーザー名をテンプレートに渡す
		model.addAttribute("accountName", account.getAccountName());
		// blog-register.html (テンプレート) を返す
		return "blog-register";
	}

	// 投稿処理を実行するメソッド

	@PostMapping("/blog/register/process")
	public String processRegister(@RequestParam("blogTitle") String blogTitle,
			@RequestParam("categoryName") String categoryName, @RequestParam("blogImage") MultipartFile blogImage,
			@RequestParam("article") String article) {
		// セッションからログイン中ユーザー情報を取得
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			// 未ログインならログイン画面へリダイレクト
			return "redirect:/login";
		}

		// 画像ファイル名にタイムスタンプを付与
		String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String fileName = timestamp + "-" + blogImage.getOriginalFilename();

		try {
			// static/blog-image 以下にファイルを保存
			Path destPath = Paths.get("src/main/resources/static/blog-image/" + fileName);
			Files.copy(blogImage.getInputStream(), destPath);

			// サービス層で DB 登録
			boolean success = blogService.createBlog(blogTitle, categoryName, fileName, article,
					account.getAccountId());
			if (success) {
				// 登録成功ならホーム画面へリダイレクト
				return "redirect:/home";
			} else {
				// 登録失敗なら再び登録画面へ
				return "blog-register";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// エラー発生時は登録画面に戻す
			return "blog-register";
		}
	}
}

package blog_project.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import blog_project.com.models.entity.Account;
import blog_project.com.models.entity.Blog;
import blog_project.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogHomeController {

	@Autowired
	private HttpSession session;
	@Autowired
	private BlogService blogService;

	// ホーム画面を表示するメソッド
	@GetMapping("/home")
	public String getHomePage(Model model) {
		// セッションからログイン中ユーザー情報を取得
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			// 未ログインの場合はログイン画面へリダイレクト
			return "redirect:/login";
		}

		// ログインユーザー名をモデルに追加
		model.addAttribute("accountName", account.getAccountName());

		// ブログ一覧を取得してモデルに追加
		List<Blog> blogList = blogService.selectAllBlogList(account.getAccountId());
		model.addAttribute("blogList", blogList);

		// home.html テンプレートを返す
		return "home";
	}
}

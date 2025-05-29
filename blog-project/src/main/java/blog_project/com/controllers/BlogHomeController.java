package blog_project.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

		// ブログ一覧を取得してモデルに追加（全ユーザーの公開記事／管理者投稿を含む）
		List<Blog> blogList = blogService.findAllPublicBlogs();  
		model.addAttribute("blogList", blogList);

		// home.html テンプレートを返す
		return "home";
	}

	// 検索機能用のエンドポイント
	@GetMapping("/search")
	public String search(HttpSession session, Model model, @RequestParam(value = "q", required = false) String q) {
		// ログインチェック
		Account account = (Account) session.getAttribute("account");
		if (account == null) {
			// 未ログインならリダイレクト
			return "redirect:/login";
		}

		// ログインユーザー名（既存機能）
		model.addAttribute("accountName", account.getAccountName());
		// 検索キーワードを入力欄に保持
		model.addAttribute("q", q);

		// 検索 or 全件取得
		List<Blog> blogList;
		if (q == null || q.isBlank()) {
			// キーワードなし：既存の全件取得メソッドを呼び出し
			blogList = blogService.findAllPublicBlogs();
		} else {
			// キーワードあり：追加した検索メソッドを呼び出し
			blogList = blogService.searchByTitle(q);
		}
		model.addAttribute("blogList", blogList);
		return "home";
	}
}

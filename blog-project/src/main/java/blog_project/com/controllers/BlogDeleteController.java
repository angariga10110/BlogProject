package blog_project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog_project.com.models.entity.Account;
import blog_project.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogDeleteController {
	
	// ブログ削除用サービスを注入
	@Autowired
	private BlogService blogService;
	// セッションからログイン情報を取得
	@Autowired
	private HttpSession session;
	
	//ブログ削除処理
	@PostMapping("/blog/delete")
	public String blogDelete(@RequestParam Long blogId) {
		// ログインチェック
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			// ブログ処理実行
			if (blogService.deleteBlog(blogId)) {
				// 削除成功ならホームへ
				return "redirect:/home";
			} else {
				// 削除失敗なら編集画面へ
				return "redirect:/blog-edit" + blogId;
			}
		}
	}

}

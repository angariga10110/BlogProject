package blog_project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import blog_project.com.models.entity.Account;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogHomeController {
	@Autowired
	private HttpSession session;

	// 商品一覧画面表示
	@GetMapping("/home")
	public String getHomePage(Model model) {
		// セッションからログインしている人の情報を取得
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし、account==nullログイン画面にリダイレクトする
		// そうっではない場合
		// 商品一覧のhtmlを表示して、ログインしている人の名前の情報を画面に渡す
		if(account == null) {
			return"redirect:/login";
		}else {
			//商品の情報を取得する
//			List<blog>blogList = blogService.selectAllBlogList(account.getAccountId());
			model.addAttribute("accountName", account.getAccountName());
//			model.addAttribute("blogList",blogList);
			return"home.html";
		}
	}

}

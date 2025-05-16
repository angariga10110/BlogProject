package blog_project.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import blog_project.com.models.entity.Account;
import blog_project.com.models.entity.Blog;
import blog_project.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogEditController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private HttpSession session;

	// 編集編集画面を表示する

	@GetMapping("/blog/edit/{blogId}")
	public String showEditForm(@PathVariable("blogId") Long blogId, Model model) {
		// セッションからログインしているユーザー情報を取得
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			// 未ログインならログイン画面へリダイレクト
			return "redirect:/login";
		} else {

			// IDでブログを取得し編集権限チェック
			Blog blog = blogService.blogEditcheck(blogId);
			if (blog == null || !blog.getAccountId().equals(account.getAccountId())) {
				// 存在しない、または他人の投稿ならホームへリダイレクト
				return "redirect:/home";
			} else {

				// モデルにアカウント名とブログ情報を追加
				model.addAttribute("accountName", account.getAccountName());
				model.addAttribute("blog", blog);
				return "blog-edit";
			}

		}
	}

	// 更新処理する
	@PostMapping("/blog/edit/process")
	public String processEdit(@RequestParam String blogTitle, @RequestParam String categoryName,
			@RequestParam MultipartFile blogImage, @RequestParam String article, @RequestParam Long blogId) {
		// セッションからログインしている人の情報をadminという変数に格納
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			String fileName = new SimpleDateFormat("yyy-MM-dd-HH-mm-ss-").format(new Date())
					+ blogImage.getOriginalFilename();

			try {
				Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-image/" + fileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (blogService.updateBlog(blogId, blogTitle, categoryName, article, fileName, account.getAccountId())) {
				return "redirect:/home";
			} else {
				return "redirect:/blog-edit" + blogId;
			}
		}

	}
}
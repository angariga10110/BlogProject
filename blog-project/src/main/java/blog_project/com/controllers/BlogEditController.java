package blog_project.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	// ブログ操作用サービスを注入
	@Autowired
	private BlogService blogService;
	// セッションからログイン情報を取得
	@Autowired
	private HttpSession session;
	// アップロード先ディレクトリを取得
	@Value("${file.upload-dir}")
	private String uploadDir;

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
			@RequestParam MultipartFile blogImage, @RequestParam String article, @RequestParam Long blogId)
					throws IOException {
		// セッションからログインしている人の情報をadminという変数に格納
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			       // 既存データ取得
			       Blog blog = blogService.findById(blogId);
			
			    // タイトル／カテゴリ／本文を更新
			       blog.setBlogTitle(blogTitle);
			blog.setCategoryName(categoryName);
			blog.setArticle(article);
			
			// 画像が選択されていればアップロード
			if (!blogImage.isEmpty()) {
			String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date());
			String fileName = timestamp + blogImage.getOriginalFilename();
			Path target = Path.of(uploadDir, fileName);
			// ディレクトリ作成
			Files.createDirectories(target.getParent());  
			Files.copy(blogImage.getInputStream(),
			target,
			StandardCopyOption.REPLACE_EXISTING);
			// 画像パスをセット
			blog.setBlogImage(fileName);
			}
			// データ保存
			blogService.save(blog);
			return "redirect:/home";			   
    } 
  }
}

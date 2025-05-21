package blog_project.com.controllers;

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
	// アップロード先ディレクトリを取得
	@Value("${file.upload-dir}")
	private String uploadDir;

	
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
			@RequestParam("article") String article,Model model) {
		// セッションからログイン中ユーザー情報を取得
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			// 未ログインならログイン画面へリダイレクト
			return "redirect:/login";
		}
		
		model.addAttribute("accountName", account.getAccountName());
		if (blogImage.isEmpty()) {
            model.addAttribute("errorMsg", "请选择要上传的图片");
            return "blog-register";
        }

		// 画像ファイル名にタイムスタンプを付与
		String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String original = Path.of(blogImage.getOriginalFilename()).getFileName().toString();
		String fileName = timestamp + "-" + blogImage.getOriginalFilename();

		try {
			// static/blog-image 以下にファイルを保存
			Path target = Path.of(uploadDir, fileName);
			Files.createDirectories(target.getParent());
			Files.copy(blogImage.getInputStream(),
			           target,
			           StandardCopyOption.REPLACE_EXISTING);

			// サービス層で DB 登録
			boolean success = blogService.createBlog(blogTitle, categoryName, fileName, article,
					account.getAccountId());
			if (success) {
				// 登録成功ならホーム画面へリダイレクト
				return "redirect:/home";
			} else {
				model.addAttribute("errorMsg", "保存到数据库失败，请稍后重试");
				// 登録失敗なら再び登録画面へ
				return "blog-register";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// エラー発生時は登録画面に戻す
			model.addAttribute("errorMsg", "文件上传或保存时发生异常");
			return "blog-register";
		}
	}
}

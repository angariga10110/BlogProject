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
	@Autowired
	private BlogService blogService;
	@Autowired
	private HttpSession session;

	@PostMapping("/blog/delete")
	public String blogDelete(@RequestParam Long blogId) {
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			if (blogService.deleteBlog(blogId)) {
				return "redirect:/home";
			} else {
				return "redirect:/blog-edit" + blogId;
			}
		}
	}

}

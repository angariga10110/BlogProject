package blog_project.com.controllers;

import java.util.List;

import blog_project.com.models.entity.Account;
import blog_project.com.models.entity.Blog;
import blog_project.com.services.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BlogService blogService;  // ← 注入 BlogService

    /**
     * 普通用户登录后的首页
     * 只有登录且角色为 USER 才能访问
     */
    @GetMapping("/dashboard")
    public String showUserHome(Model model) {
        Account account = (Account) session.getAttribute("loginAccountInfo");
        if (account == null || !"USER".equals(account.getRole())) {
            // 未登录或非用户角色 -> 重定向回登录页
            return "redirect:/login";
        }

        // 1) 把当前用户名放到模板
        model.addAttribute("accountName", account.getAccountName());
        model.addAttribute("account", account);

        // 2) 获取所有公开博客（包括管理员上传的）
        List<Blog> blogList = blogService.findAllPublicBlogs();
        model.addAttribute("blogList", blogList);

        // 3) 渲染 user-home.html
        return "user-home";
    }
}

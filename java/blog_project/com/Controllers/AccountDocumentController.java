package blog_project.com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountDocumentController {
	
	@GetMapping("/document")
    public String getAccountDocumentPage(Model model) {       
        return "introduction"; 
    }
}
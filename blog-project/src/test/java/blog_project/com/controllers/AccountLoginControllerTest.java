package blog_project.com.controllers;

import blog_project.com.models.entity.Account;
import blog_project.com.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountLoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	private Account normalUser;
	private Account adminUser;

	// テスト用データ準備
	@BeforeEach
	public void setup() {
		// 観覧者
		normalUser = new Account();
		normalUser.setAccountName("Mars");
		normalUser.setRole("USER");
		// 管理者
		adminUser = new Account();
		adminUser.setAccountName("Admin");
		adminUser.setRole("ADMIN");

		// ログイン成功
		when(accountService.loginCheck("user@mail.com", "userpass")).thenReturn(normalUser);

		when(accountService.loginCheck("admin@mail.com", "adminpass")).thenReturn(adminUser);
		// ログイン失敗
		when(accountService.loginCheck("wrong@mail.com", "userpass")).thenReturn(null);

		when(accountService.loginCheck("user@mail.com", "wrongpass")).thenReturn(null);
	}

	// login画面の表示確認
	  @Test
	    public void testLoginPage() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("login"))
	                .andExpect(model().attributeDoesNotExist("errorMessage"));
	    }
	
	  // エラーメッセージ画面の表示確認
	  @Test
	  public void testLoginPageWithError() throws Exception {
	      mockMvc.perform(MockMvcRequestBuilders.get("/login?error"))
	              .andExpect(status().isOk())
	              .andExpect(view().name("login"))
	              .andExpect(model().attributeExists("errorMessage"))
	              .andExpect(model().attribute("errorMessage", "ログインに失敗しました。EmailかPasswordご確認ください。"));
	  }
	  
	// 観覧者ログイン成功
	@Test
	public void testUesrLoginSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/account/login/process").param("accountEmail", "user@mail.com")
				.param("password", "userpass")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard"));
	}
	// 管理者ログイン成功
	@Test
	public void testAdminLoginSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/account/login/process").param("accountEmail", "admin@mail.com")
				.param("password", "adminpass")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/home"));
	}
	// メールアドレス間違い
	@Test
	public void testLoginEmailIncorrect() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/account/login/process").param("accountEmail", "wrong@mail.com")
				.param("password", "userpass")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error"));
	}

	// パスワード間違い
	@Test
	public void testLoginPasswordIncorrect() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/account/login/process").param("accountEmail", "user@mail.com")
				.param("password", "wrongpass")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error"));
	}

	// 両方間違い
	@Test
	public void testLoginFailRedirect() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/account/login/process").param("accountEmail", "wrong@mail.com")
				.param("password", "wrongpass")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error"));
	}

}
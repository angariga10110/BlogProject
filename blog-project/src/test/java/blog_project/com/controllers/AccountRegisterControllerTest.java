package blog_project.com.controllers;


import blog_project.com.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AccountService accountService;

	@BeforeEach
	public void setup() {
		when(accountService.createAccount(eq("Mars"), eq("angariga@gmail.com"), eq("qqqq"), eq("USER")))
				.thenReturn(true);
		when(accountService.createAccount(eq("Anga"), eq("zms@gmail.com"), eq("aaaa"), eq("USER"))).thenReturn(false);
	}

	// register画面表示のテスト
	@Test
    public void testRegisterPageView() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/register");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"));
    }
	//モデルにroleが無い場合、自動的にUSERが設定されるテスト
	@Test
    public void testRoleDefaultUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(model().attribute("role", "USER"));
    }
	 //roleが不正な場合は上書きされないこと確認テスト
	 @Test
	    public void testRoleNotOverwritten() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.get("/register").flashAttr("role", "ADMIN"))
	                .andExpect(model().attribute("role", "ADMIN"));
	    }
	 //パスワードが一致しない場合のテスト
	 @Test
	    public void testPasswordMismatch() throws Exception {
	        RequestBuilder request = MockMvcRequestBuilders.post("/account/register/process")
	                .param("accountName", "test")
	                .param("accountEmail", "test@test.com")
	                .param("password", "abc")
	                .param("confirmPassword", "def")
	                .param("role", "USER");

	        mockMvc.perform(request)
	                .andExpect(view().name("register.html"));
	    }
	 //パスワード不一致時の入力保持のテスト
	 @Test
	    public void testPasswordMismatchModelAttributes() throws Exception {
	        RequestBuilder request = MockMvcRequestBuilders.post("/account/register/process")
	                .param("accountName", "test")
	                .param("accountEmail", "test@test.com")
	                .param("password", "abc")
	                .param("confirmPassword", "def")
	                .param("role", "USER");

	        mockMvc.perform(request)
	                .andExpect(model().attributeExists("errorMessage"))
	                .andExpect(model().attribute("accountName", "test"))
	                .andExpect(model().attribute("accountEmail", "test@test.com"))
	                .andExpect(model().attribute("role", "USER"))
	                .andExpect(view().name("register.html"));
	    }
	 //Marsアカウント正常登録のテスト
	 @Test
	    public void testRegisterMarsSuccess() throws Exception {
	        RequestBuilder request = MockMvcRequestBuilders.post("/account/register/process")
	                .param("accountName", "Mars")
	                .param("accountEmail", "angariga@gmail.com")
	                .param("password", "qqqq")
	                .param("confirmPassword", "qqqq")
	                .param("role", "USER");

	        mockMvc.perform(request)
	                .andExpect(redirectedUrl("/login"));

	 }
	 // No.8 登録失敗した場合
	    @Test
	    public void testRegisterFailure() throws Exception {
	        RequestBuilder request = MockMvcRequestBuilders.post("/account/register/process")
	                .param("accountName", "failuser")
	                .param("accountEmail", "fail@test.com")
	                .param("password", "aaaa")
	                .param("confirmPassword", "aaaa")
	                .param("role", "USER");

	        mockMvc.perform(request)
	                .andExpect(view().name("register.html"))
	                .andExpect(model().attributeExists("errorMessage"));
	    }
	 // No.9 成功時はリダイレクトする
	    @Test
	    public void testCreateAccountSuccess() throws Exception {
	        	        RequestBuilder request = MockMvcRequestBuilders.post("/account/register/process")
	                .param("accountName", "Mars")
	                .param("accountEmail", "angariga@gmail.com")
	                .param("password", "qqqq")
	                .param("confirmPassword", "qqqq")
	                .param("role", "USER");
	        mockMvc.perform(request)
	                .andExpect(redirectedUrl("/login"));
	    }
}

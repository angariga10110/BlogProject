package blog_project.com.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Account {

	// account_idの設定
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long accountId;
	// account_nameの設定
	private String accountName;
	// account_emailの設定
	private String accountEmail;
	// passwordの設定
	private String password;
	
	@Column(nullable = false)
    private String role = "USER";

	// 空のコンストラクタ
	public Account() {
	}

	// コンストラクタ
	public Account( String accountName, String accountEmail, String password,String role) {
		this.accountName = accountName;
		this.accountEmail = accountEmail;
		this.password = password;
		this.role = role;
	}

	// ゲッター・センター
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

}

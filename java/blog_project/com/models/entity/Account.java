package blog_project.com.models.entity;

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

	// 空のコンストラクタ
	public Account() {
	}

	// コンストラクタ
	public Account( String accountName, String accountEmail, String password) {
		this.accountName = accountName;
		this.accountEmail = accountEmail;
		this.password = password;
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

}

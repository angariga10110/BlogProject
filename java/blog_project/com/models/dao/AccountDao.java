package blog_project.com.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog_project.com.models.entity.Account;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

	// 保存処理と更新処理 insertとupdate
	Account save(Account account);
	// SELECT * FROM account WHERE account_email = ?
	// 用途;管理者の登録処理をする時に、同じメールアドレスがあったら登録させないようにする
	// 1行だけしかレコード取得できない
    Account findByAccountEmail(String accountEmail);
	// SELECT * FROM account WHERE account_email=? AND password=?
	// 用途;ログイン処理に使用、入力したメールアドレスとパスワードが一致してるだけデータを取得する
    Account findByAccountEmailAndPassword(String accountEmail,String passeord);
}

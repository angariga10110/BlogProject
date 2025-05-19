package blog_project.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog_project.com.models.dao.AccountDao;
import blog_project.com.models.entity.Account;

@Service
public class AccountService {

	@Autowired
	private AccountDao accountDao;

	// 保存処理 登録処理
	// もし、findByAccountEmail==mull だったら登録処理をします
	// saveメソッドを使用して登録処理をする
	// 保存ができたらtrue
	// そうでない場合、保存処理失敗 false
	public boolean createAccount(String accountName, String accountEmail, String password) {
		if (accountDao.findByAccountEmail(accountEmail) == null) {
			accountDao.save(new Account(accountName, accountEmail, password));
			return true;
		} else {
			return false;
		}
	}

	// ログイン処理
	// もし、emailとpasswordがfindByEmailAndPasswordを使用して存在しない場合(==null)
	// その場合、存在しないnullであるをコントローラークラスに知らせる
	// そうでない場合ログインしている人の情報をコントローラークラス渡す
	public Account loginCheck(String accountEmail, String password) {
		Account account = accountDao.findByAccountEmailAndPassword(accountEmail, password);
		if (account == null) {
			return null;
		} else {
			return account;
		}
	}

}

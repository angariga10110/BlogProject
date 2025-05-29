package blog_project.com.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog_project.com.models.entity.Blog;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface BlogDao extends JpaRepository<Blog, Long> {

	// 保存処理と更新処理 insertとupdate
	Blog save(Blog blog);

	// SELECT * FROM blog
	// 用途 一覧を表示させるときに使用
	List<Blog> findAll();

	// カテゴリ名で検索（重複チェック用）
	Blog findByCategoryName(String categoryName);
	// カテゴリ名の存在確認（重複チェック用）
	boolean existsByCategoryName(String categoryName);

	// SELECT * FROM blog WHERE blog_id = ?
	// 用途;ブログIDで取得（編集画面表示用）
	Blog findByBlogId(Long blogId);

	// DLETE FROM blog WHERE blog_id = ?
	// 用途;ブログIDで削除（削除処理用）
	void deleteByBlogId(Long blogId);
	
	//ブログタイトルを部分一致検索（大文字小文字を無視）
	List<Blog> findByBlogTitleContainingIgnoreCase(String blogTitle);
}

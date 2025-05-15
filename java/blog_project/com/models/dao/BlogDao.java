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
	// 用途;商品の一覧を表示させるときに使用
	List<Blog> findAll();

	// SELECT * FROM blog WHERE blog_name = ?
	// 用途;商品の登録チックに使用 同じ商品登録されないようにするチックに使用
	Blog findByCategoryName(String categoryName);

	// SELECT * FROM blog WHERE blog_id = ?
	// 用途;編集画面を表示する際に使用。
	Blog findByBlogId(Long blogId);

	// DLETE FROM blog WHERE blog_id = ?
	// 用途;削除使用します。
	void deleteByBlogId(Long blogId);
}

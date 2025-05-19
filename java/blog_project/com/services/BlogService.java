package blog_project.com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog_project.com.models.dao.BlogDao;
import blog_project.com.models.entity.Blog;

@Service
public class BlogService {
	
	@Autowired
	BlogDao blogDao;
	
	//IDでブログを取得。存在しなければ例外を投げる
	public Blog findById(Long blogId) {
		return blogDao.findById(blogId).orElseThrow(() -> new RuntimeException("指定のブログが見つかりません: " + blogId));
	}
	//ブログを保存
	public Blog save(Blog blog) {
		return blogDao.save(blog);
	}

	// 商品一覧のチック
	// もし、accountId == null 戻り値としてnull
	// findall内容をコントローラークラスに渡す
	public List<Blog> selectAllBlogList(Long accountId) {
		if (accountId == null) {
			return null;
		} else {
			return blogDao.findAll();
		}
	}

	// 商品登録処理のチック
	// もし、findByCategoryNameが ==nullだったら
	// 保存処理
	// そうでない場合、
	// false
	public boolean createBlog(String blogTitle, String categoryName, String blogImage, String article, Long accountId) {
		if (!blogDao.existsByCategoryName(categoryName)) {
            blogDao.save(new Blog(blogTitle, categoryName, blogImage, article, accountId));
            return true;
		} else {
			return false;
		}
	}

	// 編集画面を表示する時のチェック
	// もし、blogId == null
	// そうではない場合
	// findByBlogIdの情報をコントローラークラスに渡す
	public Blog blogEditcheck(Long blogId) {
		if (blogId == null) {
			return null;
		} else {
			return blogDao.findByBlogId(blogId);
		}
	}

	// 更新処理のチェック
	// blogId==null , 更新処理しない
	// コントローラークラスからもらったらblogIdを使って、編集する前のデータを取得
	// 変更するべきどころだけ、セッターを使用してデータを更新する
	public boolean updateBlog(Long blogId, String blogTitle, String categoryName, String blogImage, String article,
			Long accountId) {
		if (blogId == null) {
			return false;
		} else {
			Blog blog = blogDao.findByBlogId(blogId);
			blog.setBlogTitle(blogTitle);
			blog.setCategoryName(categoryName);
			blog.setBlogImage(blogImage);
			blog.setArticle(article);
			blogDao.save(blog);

			return true;
		}
	}

	// 削除処理のチェック
	public boolean deleteBlog(Long blogId) {
		if (blogId == null) {
			return false;
		} else {
			blogDao.deleteByBlogId(blogId);
			return true;
		}
	}
}
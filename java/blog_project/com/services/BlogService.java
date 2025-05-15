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
	public boolean createBlog(String blogTitle,String categoryName,String blogImage,
			String article,Long accountId) {
		if(blogDao.findByCategoryName(categoryName)==null) {
		   blogDao.save(new Blog(blogTitle,categoryName,blogImage,article,accountId));
		   return true;
		}else {
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}

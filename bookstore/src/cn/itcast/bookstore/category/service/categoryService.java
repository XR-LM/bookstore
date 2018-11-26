package cn.itcast.bookstore.category.service;

import java.util.List;

import cn.itcast.bookstore.book.dao.BookDao;
import cn.itcast.bookstore.category.dao.categoryDao;
import cn.itcast.bookstore.category.domain.category;
import cn.itcast.bookstore.category.web.servlet.admin.CategoryException;

public class categoryService {
	private categoryDao catdao =new categoryDao();
	private BookDao bookDao =new BookDao();
	//查询所有分类
	public  List<category> findAll() {
		return catdao.findAll();
	}
	//添加分类
	public void add(category cate) {
		// TODO Auto-generated method stub
		catdao.add(cate);
	}
	public void delete(String cid) throws CategoryException{
		// TODO Auto-generated method stub
		// 获取该分类下图书的本数
		int count = bookDao.getCountByCid(cid);
		// 如果该分类下存在图书，不让删除，我们抛出异常
		if(count > 0) throw new CategoryException("该分类下还有图书，不能删除！");
		// 删除该分类
		catdao.delete(cid);
	}
	public category load(String cid) {
		// TODO Auto-generated method stub
		return catdao.load(cid);
	}
	//修改分类
	public void edit(category cate) {
		// TODO Auto-generated method stub
		 catdao.edit(cate);
	}
}

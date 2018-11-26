package cn.itcast.bookstore.book.service;

import java.util.List;

import cn.itcast.bookstore.book.dao.BookDao;
import cn.itcast.bookstore.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();
	//查询所有图书
	public List<Book> findAll(){
		return bookDao.findAll();
	}
	//按类别查询
	public List<Book> findByCategory(String cid){
		return bookDao.findByCategory(cid);
	}
	public Book load(String bid) {
		return bookDao.load(bid);
	}
	public void add(Book book) {
		// TODO Auto-generated method stub
		bookDao.add(book);
	}
	//删除图书
	public void delete(String bid){
		bookDao.delete(bid);
	}
	//修改图书
	public void edit(Book book) {
		// TODO Auto-generated method stub
		bookDao.edit(book);
	}
}

package cn.itcast.bookstore.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.bookstore.category.service.categoryService;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService =new BookService();
	//查询所有图书
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());	
		return "f:/jsps/book/list.jsp";
	}
	//查询指定类别的图书
	public String findByCategory(HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
		//获取类别编号
		String cid= request.getParameter("cid");
		request.setAttribute("bookList", bookService.findByCategory(cid));
		return "f:/jsps/book/list.jsp";
	}
	//查询一本书的详细信息
	
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//将图书信息保存到request域中
		request.setAttribute("book", bookService.load(request.getParameter("bid")));
		return "f:/jsps/book/desc.jsp";
	}
}

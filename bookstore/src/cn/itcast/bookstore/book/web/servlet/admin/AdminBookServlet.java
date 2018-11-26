package cn.itcast.bookstore.book.web.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.bookstore.category.domain.category;
import cn.itcast.bookstore.category.service.categoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService =new BookService();
	private categoryService cate =new categoryService();
	/*
	 * 添加图书前
	 */
	public String addPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 查询所有分类，保存到request，转发到add.jsp
		 * add.jsp中把所有的分类使用下拉列表显示在表单中
		 */
		request.setAttribute("categoryList", cate.findAll());
		return "f:/adminjsps/admin/book/add.jsp";
	}
	/**
	 *查询所有图书
	 */
	public String findAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		return "f:/adminjsps/admin/book/list.jsp";
	}
	/*
	 * 查询图书详情
	 * 1、获取图书bid
	 * 2、通过service查询图书详情
	 * 3、将查询的结果保存到request域中
	 * 4、将页面转发到desc.jsp
	 */
	public String load(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		//查询所有分类
		request.setAttribute("category",cate.findAll());
		String bid = request.getParameter("bid");
		request.setAttribute("book",bookService.load(bid));
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	/*
	 * 删除图书
	 * 1、获取bid
	 * 2、调用service的delete方法
	 * 3、查询所有图书
	 */
	public String delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String bid =request.getParameter("bid");
		bookService.delete(bid);
		return findAll(request, response);
	}
	/*
	 * 编辑图书
	 * 1、获取图书bid
	 * 2、封装图书信息
	 * 3、补全cid
	 * 4、调用service的update方法
	 * 5、查询所有图书
	 */
	public String edit(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String bid =request.getParameter("bid");
		Book book =CommonUtils.toBean(request.getParameterMap(), Book.class);
		category cate =CommonUtils.toBean(request.getParameterMap(), category.class);
		book.setCategory(cate);
		bookService.edit(book);
		return findAll(request, response);
	}
}

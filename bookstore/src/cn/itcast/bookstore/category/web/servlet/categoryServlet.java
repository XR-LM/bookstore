package cn.itcast.bookstore.category.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.itcast.bookstore.category.service.categoryService;
import cn.itcast.servlet.BaseServlet;

public class categoryServlet extends BaseServlet {
	private categoryService catservice =new categoryService();
	//查询所有分类
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//通过service查询所有图书类别
		request.setAttribute("catlist", catservice.findAll());
		return "f:/jsps/left.jsp";
	}
}

package cn.itcast.bookstore.category.web.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.category.domain.category;
import cn.itcast.bookstore.category.service.categoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminServlet extends BaseServlet {
	private categoryService categoryService =new categoryService();
	//查看所有分类
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 调用service的findAll方法查询所有分类
		 * 将查询结果保存到request域中
		 * 将页面转发到adminjsp/list.jsp页面
		 */
		request.setAttribute("category", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	//添加分类
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 封装表单
		 * 补全分类信息
		 * 调用service方法保存分类
		 * 调用findAll
		 */
		category cate =CommonUtils.toBean(request.getParameterMap(), category.class);
		cate.setCid(CommonUtils.uuid());
		categoryService.add(cate);
		
		return findAll(request, response);
	}
	//查看指定分类(修改之前的加载工作)
	public String preEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	String cid =request.getParameter("cid");
	request.setAttribute("category", categoryService.load(cid));
	
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	//修改分类
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		category cate =CommonUtils.toBean(request.getParameterMap(), category.class);
		categoryService.edit(cate);
		return findAll(request, response);	}
	//删除分类
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取cid
		 * 查询该分类下面有没有图书
		 * 如果没有图书调用service的delete方法删除该分类
		 * 调用findAll方法
		 * 如果有图书则输出错误信息
		 */
		String cid = request.getParameter("cid");
		try {
			categoryService.delete(cid);
			return findAll(request, response);
		} catch(CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
	}
}

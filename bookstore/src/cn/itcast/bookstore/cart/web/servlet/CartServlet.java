package cn.itcast.bookstore.cart.web.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.bookstore.cart.domain.Cart;
import cn.itcast.bookstore.cart.domain.CartItem;
import cn.itcast.servlet.BaseServlet;

public class CartServlet extends BaseServlet {
	//获取购物车
	//向购物车中添加条目
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart=(Cart)request.getSession().getAttribute("cart");
		//获取图书
		String bid = request.getParameter("bid");
	    Book book =new BookService().load(bid);
	    //获取购买图书数量
	    int count =Integer.parseInt(request.getParameter("count"));
	    //得到图书条目对象
	    CartItem cartItem =new CartItem();
	    cartItem.setBook(book);
	    cartItem.setCount(count);
	    //添加条目到购物车
	    cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";
	}
	//删除指定条目
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart=(Cart)request.getSession().getAttribute("cart");
		cart.delete(request.getParameter("bid"));
		return "f:/jsps/cart/list.jsp";
	}
	//清空购物车
	public String clear(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart=(Cart)request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
}

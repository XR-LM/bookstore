package cn.itcast.bookstore.order.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.order.domain.Order;
import cn.itcast.bookstore.order.service.OrderService;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService =new OrderService();
	/**
	 * 查询用户的所有订单
	 */
	
	public String findAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Order> orderlist=orderService.findAll();
		request.setAttribute("orderlist", orderlist);
		return "f:/adminjsps/admin/order/list.jsp";
		// Put your code here
	}
	/**
	 * 发货
	 * 1、获取订单id
	 * 2、调用service的方法更改订单状态
	 * 3、保存成功信息
	 * 4、将页面跳转到查询所有订单
	 */
	public String sendorder(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		orderService.sendorder(oid);
		return findAll(request, response);
		// Put your code here
	}

}

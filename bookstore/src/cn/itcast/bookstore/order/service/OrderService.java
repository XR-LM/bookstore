package cn.itcast.bookstore.order.service;
import java.sql.SQLException;
import java.util.List;

import cn.itcast.bookstore.order.dao.OrderDao;
import cn.itcast.bookstore.order.domain.Order;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao orderdao =new OrderDao();
	public void add(Order order) {
		// TODO Auto-generated method stub
		try {
			JdbcUtils.beginTransaction();
			orderdao.add(order);
			orderdao.addOrderItem(order.getOrderItemList());
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				 throw new RuntimeException(e);
			}
		}
	}
	public List<Order> myOrder(String uidString) {
		// TODO Auto-generated method stub
		return orderdao.findByUid(uidString);
	}
	//付款
	public Order load(String parameter) {
		// TODO Auto-generated method stub
		return orderdao.load(parameter);
	}
	//修改订单状态
	public void confirm(String oid)throws OrderException{
		//查询订单状态是否为3
		int state = orderdao.orderState(oid);
		if(state!=3) throw new OrderException("你还未支付！");
		//支付成功
		orderdao.updateState(oid, 4);
	}
	public void zhifu(String oid) {
		/*
		 * 1. 获取订单的状态
		 *   * 如果状态为1，那么执行下面代码
		 *   * 如果状态不为1，那么本方法什么都不做
		 */
		int state = orderdao.orderState(oid);
		if(state == 1) {
			// 修改订单状态为2
			orderdao.updateState(oid, 2);
		}
	}
	//查询所有订单
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		return orderdao.findAll();
	}
	public void sendorder(String oid) {
		// TODO Auto-generated method stub
		   orderdao.checked(oid);
	}
	
}

package cn.itcast.bookstore.order.domain;

import java.util.Date;
import java.util.List;

import cn.itcast.bookstore.user.domain.User;

//订单类
public class Order {
	private  String oid;
	private Date ordertime;//下单时间
	private double total;//总金额
	private int state;//订单状态
	private User user;//用户
	private String address;//地址
	
	private List<OrderItem> orderItemList;//当前订单下所有条目

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}

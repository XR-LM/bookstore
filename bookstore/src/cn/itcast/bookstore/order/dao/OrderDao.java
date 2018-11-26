package cn.itcast.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.order.domain.Order;
import cn.itcast.bookstore.order.domain.OrderItem;
import cn.itcast.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	public void add(Order order) {
		try {
		// TODO Auto-generated method stub
		String sql ="insert into orders values(?,?,?,?,?,?)";
		//设置时间
		Timestamp time = new Timestamp(order.getOrdertime().getTime());
		Object[] params={order.getOid(),time,order.getTotal(),order.getState(),order.getUser().getUid(),order.getAddress()};
			qr.update(sql,params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public void addOrderItem(List<OrderItem> orderItemList) {
		// TODO Auto-generated method stub
		/**
		 * QueryRunner类的batch(String sql, Object[][] params)
		 * 其中params是多个一维数组！
		 * 每个一维数组都与sql在一起执行一次，多个一维数组就执行多次
		 */
		try {
			String sql = "insert into orderitem values(?,?,?,?,?)";
			/*
			 * 把orderItemList转换成二维数组
			 *   把一个OrderItem对象转换成一个一维数组
			 */
			Object[][] params = new Object[orderItemList.size()][];
			// 循环遍历orderItemList，使用每个orderItem对象为params中每个一维数组赋值
			for(int i = 0; i < orderItemList.size(); i++) {
				OrderItem item = orderItemList.get(i);
				params[i] = new Object[]{item.getIid(), item.getCount(), 
						item.getSubtotal(), item.getOrder().getOid(),
						item.getBook().getBid()}; 
			}
			qr.batch(sql, params);//执行批处理
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//查询指定用户的所有订单
	//查询所有的订单条目
	public List<Order> findByUid(String uidString) {
		// TODO Auto-generated method stub
		
		String sql ="select * from orders where uid =?";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class),uidString);
			for (Order order : orderList) {
				LoadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//加载每一个订单的订单条目
	private void LoadOrderItems(Order order) {
		// TODO Auto-generated method stub
		String sql ="select * from orderitem i,book b where i.bid=b.bid and oid =?";
		try {
			List<Map<String,Object>> mapList =qr.query(sql, new MapListHandler(),order.getOid());
			//循环两个Map，oderitem,book,将结果保存到两个对象中，然后建立两者关系
			List<OrderItem> orderItemList =toOrderItemList(mapList);
		    order.setOrderItemList(orderItemList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//循环Map，oderitem,book,将结果保存到两个对象中，然后建立两者关系
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (Map<String, Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}
	//把一个map转化为一个orderitem
	private OrderItem toOrderItem(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Book book =CommonUtils.toBean(map, Book.class);
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		orderItem.setBook(book);
		return orderItem;
	}
	//通过oid查询订单
	public Order load(String oid) {
		String sql ="select * from orders where oid =?";
		try {
			Order order = qr.query(sql,new BeanHandler<Order>(Order.class),oid);
			LoadOrderItems(order);
			return order;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//查询订单状态
	public int orderState(String oid){
		String sqlString ="select state from orders where oid=?";
		try {
			return (Integer)qr.query(sqlString,new ScalarHandler(),oid );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//更改订单状态
	public void updateState(String oid,int state){
		String sqlString ="update orders set state=? where oid=?";
		try {
			qr.update(sqlString,state,oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//查询所有订单
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		String sql ="select * from orders";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class));
			for (Order order : orderList) {
				LoadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public void checked(String oid) {
		String sql ="update orders set state=? where oid=?";
		try {
			qr.update(sql,3,oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}

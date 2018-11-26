package cn.itcast.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
	//用来存放书本条目
	private Map<String,CartItem> cartmap = new LinkedHashMap<String,CartItem>();
	//添加条目
	public void add(CartItem cartItem){
		//判断原来的购物车里面有没有该商品，如果存在就直接加上，没有则新建一个条目
		if(cartmap.containsKey(cartItem.getBook().getBid())){
		//获取原来的条目
		 CartItem _cartItem = cartmap.get(cartItem.getBook().getBid());
		 //更新数据
		 _cartItem.setCount(_cartItem.getCount()+cartItem.getCount());
		 cartmap.put(cartItem.getBook().getBid(), _cartItem);
		}else {
			cartmap.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	//删除指定条目
	public void delete(String bid){
		cartmap.remove(bid);
	}
	//清空购物车
	public void clear(){
		cartmap.clear();
	}
	//获取所有条目
	public Collection<CartItem> getCartItems(){
		return cartmap.values();
	}
	//计算总金额
	public double getTotal(){
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartmap.values()) {
			BigDecimal subtotal = new BigDecimal("" + cartItem.getSubtotal());
			total = total.add(subtotal);
		}
		return total.doubleValue();
	}
}

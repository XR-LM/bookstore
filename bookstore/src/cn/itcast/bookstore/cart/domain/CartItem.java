package cn.itcast.bookstore.cart.domain;

import java.math.BigDecimal;

import cn.itcast.bookstore.book.domain.Book;
//购物条目类
public class CartItem {
	private Book book;
	private int count;
	//小计
	public double getSubtotal(){
		//为了防止应二进制计算所带来的误差，使用BigDecimal类处理数据
		BigDecimal count = new BigDecimal(this.getCount()+"");//数量
		BigDecimal price =new BigDecimal(this.getBook().getPrice()+"");//单价
		return price.multiply(count).doubleValue();
		
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}

package cn.itcast.bookstore.book.domain;

import cn.itcast.bookstore.category.domain.category;

public class Book {
	private String bid;
	private String bname;
	private String price;
	private String author;
	private String image;
	private category category;
	private boolean del;
	public boolean isDel() {
		return del;
	}
	public void setDel(boolean del) {
		this.del = del;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public category getCategory() {
		return category;
	}
	public void setCategory(category category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Book [bid=" + bid + ", bname=" + bname + ", price=" + price
				+ ", author=" + author + ", image=" + image + ", category="
				+ category + "]";
	}
	
}

package cn.itcast.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.category.domain.category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr =new TxQueryRunner();
	//查询所有类别
	public List<Book> findAll(){
		try {
			String sql ="select * from book where del=false";
 			return qr.query(sql,new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	//按照类别查询
	public List<Book> findByCategory(String cid){
		try {
			String sql ="select * from book where cid =? and del=false";
			return qr.query(sql,new BeanListHandler<Book>(Book.class),cid);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	//查询图书详情
		public Book load(String bid){
			try {
				String sql ="select * from book where bid =?";
				Map<String, Object> map = qr.query(sql, new MapHandler(),bid);
				category cate =CommonUtils.toBean(map, category.class);
				Book book =CommonUtils.toBean(map, Book.class);
				book.setCategory(cate);
				return book;
			} catch (SQLException e) {
				// TODO: handle exception
				throw new RuntimeException(e);
			}
		}
		public int getCountByCid(String cid) {
			// TODO Auto-generated method stub
			String sql ="select count(*) from book where cid=?";
			try {
				Number cnt = (Number)qr.query(sql, new ScalarHandler(), cid);
				return cnt.intValue();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
		public void add(Book book) {
			String sql ="insert into book values(?,?,?,?,?,?,?)";
			try {
				Object[] params={book.getBid(),book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),book.getCategory().getCid(),false};
				qr.update(sql,params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}			
		}
		//删除图书（只需修改图书状态即可）
		public void delete(String bid){
			String sql ="update book set del=true where bid=?";
			try {
				qr.update(sql,bid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}		
		}
		public void edit(Book book) {
			// TODO Auto-generated method stub
			String sql ="update book set bname=?,price=?,author=?,image=?,cid=? where bid=?";
			Object[] params={book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),book.getCategory().getCid(),book.getBid()};
			try {
				qr.update(sql,params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
}

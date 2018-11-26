package cn.itcast.bookstore.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.bookstore.category.domain.category;
import cn.itcast.jdbc.TxQueryRunner;

public class categoryDao {
	QueryRunner qr =new TxQueryRunner();
	//查询所有图书类别
	public List<category> findAll() {
		// TODO Auto-generated method stub
		String sql ="select * from category";
		try {
			return qr.query(sql, new BeanListHandler<category>(category.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//添加分类
	public void add(category cate) {
		String sql ="insert into category values(?,?)";
		try {
			qr.update(sql,cate.getCid(),cate.getCname());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//删除分类
	public void delete(String cid) {
		// TODO Auto-generated method stub
		try {
			String sql = "delete from category where cid=?";
			qr.update(sql,cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//加载分类
	public category load(String cid) {
		// TODO Auto-generated method stub
		try {
			String sql = "select * from category where cid =?";
			return qr.query(sql,new BeanHandler<category>(category.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//修改分类
	public void edit(category cate) {
		// TODO Auto-generated method stub
		try {
			String sql = "update category set cname=? where cid =?";
			qr.update(sql,cate.getCname(),cate.getCid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

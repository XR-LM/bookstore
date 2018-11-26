package cn.itcast.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.bookstore.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {
	//创建数据库操作对象
	QueryRunner qr =new TxQueryRunner();
	//通过用户名判断判断用户是否已经被注册
	public User findUserByName(String username){
		String sql ="select * from tb_user where username=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new  RuntimeException(e);
		}
	}
	//通过用户名判断判断邮箱是否已经被注册
		public User findUserByEmail(String email){
			String sql ="select * from tb_user where email=?";
			try {
				return qr.query(sql, new BeanHandler<User>(User.class),email);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new  RuntimeException(e);
			}
		}
	//通过用注册码判断判断邮箱是否已经被注册
		public User findUserByCode(String code){
			String sql ="select * from tb_user where code=?";
			try {
				return qr.query(sql, new BeanHandler<User>(User.class),code);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new  RuntimeException(e);
			}
		}
	//通过用注册码判断判断邮箱是否已经被注册
		public void updateState(User user,boolean state){
			String sql ="update tb_user set state =? where uid =?";
			try {
			    qr.update(sql,state,user.getUid());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new  RuntimeException(e);
			}
		}
	//通过用户名判断判断邮箱是否已经被注册
	public void add(User user){
		try {
			//准备sql模板
			String sql ="insert into tb_user values(?,?,?,?,?,?)";
			//准备参数
			Object[] params = {user.getUid(), user.getUsername(), 
					user.getPassword(), user.getEmail(), user.getCode(),
					user.isState()};
			qr.update(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new  RuntimeException(e);
		}
	}
}

package cn.itcast.bookstore.user.service;

import cn.itcast.bookstore.user.dao.UserDao;
import cn.itcast.bookstore.user.domain.User;

public class UserService {
	private UserDao  userDao =new UserDao();
	//注册用户
	public void regist(User form) throws UserException{
		//调用UserDao的 finduserbyname判断用户名是否已经被注册
		User user =userDao.findUserByName(form.getUsername());
		if(user!=null) throw new UserException("该用户名已经被注册");
		//调用UserDao的finduserbyEmail方法判断邮箱是否已经被注册
		user =userDao.findUserByEmail(form.getEmail());
		if(user!=null) throw new UserException("该用户名已经被注册");
		//信息填写正确直接保存用户
		userDao.add(form);
		
		
	}
	//激活用户
	public void active(String code) throws UserException{
		// TODO Auto-generated method stub
		//在数据库中查询是否存在该激活码用户如果不存在就抛出异常
		//如果存在则将用户的激活状态改为true
		User user =userDao.findUserByCode(code);
		if(user==null) throw new UserException("激活码无效！");
		//已经激活的用户不能重复激活
		if(user.isState()) throw new UserException("您已经激活过了！");
		userDao.updateState(user,true);
	}
	//用户登录
	//校验用户名是否存在，如果不存在就抛出异常，保存异常信息和form表单信息，转发到login.jsp页面
	 // 校验密码是否正确
	public User login(User form) throws UserException{
		//查询用户是否存在
		User user = userDao.findUserByName(form.getUsername());
		if(user==null) throw new UserException("用户名错误！");
		//校验密码是否正确
		if(!form.getPassword().equals(user.getPassword()))
			throw new UserException("密码错误！");
		if(user.isState()==false) throw new UserException("您还未在邮箱中激活用户！");
		return user;
		
	}
}

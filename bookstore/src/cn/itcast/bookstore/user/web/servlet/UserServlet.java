package cn.itcast.bookstore.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.cart.domain.Cart;
import cn.itcast.bookstore.user.domain.User;
import cn.itcast.bookstore.user.service.UserException;
import cn.itcast.bookstore.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	/*
	 * 用户退出
	 * 直接销毁session
	 * 将页面重定向到index.jsp
	 */
	public String exit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().invalidate();
		return "r:/index.jsp";
	}
	
	/*
	 * 用户登录
	 * 封装表单数据
	 * 调用service中的login方法
	 * 登录成功，保存用户信息到session域中
	 * 将页面重定向到index.jsp
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装数据
		User form =CommonUtils.toBean(request.getParameterMap(), User.class);
		//登录
		String vcode =(String)request.getSession().getAttribute("vcode_session");
		String vdcode =request.getParameter("validate");
		System.out.println(vcode+"==="+vdcode);
		if (!vcode.equalsIgnoreCase(vdcode)) {
			request.setAttribute("msg", "验证码错误");
			return "f:/jsps/user/login.jsp";
		}
		
		try {
			User user=userService.login(form);
			//登陆成功保存信息到session域中
			request.getSession().setAttribute("user_session", user);
			//发车
			request.getSession().setAttribute("cart",new Cart());
			//将页面重定向到index.jsp
			return "r:/index.jsp";
		} catch (UserException e) {
			//保存错误信息和form信息到request域中
			//将页面转发到login.jsp
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", form);
			return "f:/jsps/user/login.jsp";
		}
	}

	/**
	 * 激活用户
	 */
	public String active(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 获取参数激活码 2. 调用service方法完成激活 > 保存异常信息到request域，转发到msg.jsp 3.
		 * 保存成功信息到request域，转发到msg.jsp
		 */
		String code = request.getParameter("code");
		try {
			userService.active(code);
			request.setAttribute("msg", "恭喜，您激活成功了！请马上登录！");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}

	/*
	 * 注册用户
	 */
	public String regist(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1、封装表单数据 2、校验数据是否合法，如果不合法保存错误信息和表单信息到request域中，将错误信息发回regist页面
		 * 3、调用service中的register（User）方法 4、如果该方法抛出异常将异常信息转发到msg.jsp页面并保存user数据
		 * 5、保存用户 6、发送邮件 7、激活邮件
		 */
		// 封装表单数据
		User user = CommonUtils.toBean(request.getParameterMap(), User.class);
		// 通过UUID创建用户编号,和激活码
		user.setUid(CommonUtils.uuid());
		user.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		// 获取用户名，密码和邮箱判断是否合法
		String username = user.getUsername();
		String password = user.getPassword();
		String email = user.getEmail();

		// 创建一个Map集合用来保存不同字段的错误信息
		Map<String, String> error = new HashMap<String, String>();
		if (username == null || username.trim().isEmpty()) {
			error.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 6) {
			error.put("username", "用户名长度必须为3-6个字符");
		}
		if (password == null || password.trim().isEmpty()) {
			error.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 12) {
			error.put("password", "密码长度必须为3-12个字符");
		}
		if (email == null || email.trim().isEmpty()) {
			error.put("email", "邮箱不能为空");
		} else if (!email.matches("\\w+@\\w+\\.\\w+")) {
			error.put("email", "邮箱格式不正确");
		}
		// 判断表单数据是否有错误
		if (error.size() != 0) {
			// 保存表单数据
			request.setAttribute("user", user);
			// 保存错误信息
			request.setAttribute("error", error);
			// 将页面转发到regist.jsp
			return "f:/jsps/user/regist.jsp";
		}
		// 执行service的regist（User方法）
		try {
			userService.regist(user);
		} catch (UserException e) {
			// TODO: handle exception
			/*
			 * 1. 保存异常信息 2. 保存form 3. 转发到regist.jsp
			 */
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", user);
			return "f:/jsps/user/regist.jsp";
		}
		// 发邮件
		// 获取配置文件内容
		Properties pro = new Properties();
		pro.load(this.getClass().getClassLoader()
				.getResourceAsStream("email_templete.properties"));
		String uname = pro.getProperty("uname");// 发件人姓名
		String from = pro.getProperty("from");// 发件人邮箱
		String pwd = pro.getProperty("pwd");// 密码
		String host = pro.getProperty("host");// 主机名称
		String subject = pro.getProperty("subject");// 主题
		String content = pro.getProperty("content");// 内容
		content = MessageFormat.format(content, user.getCode());// 设置激活码
		Session session = MailUtils.createSession(host, uname, pwd);// 设置session
		Mail mail = new Mail(from, user.getEmail(), subject, content);// 设置邮件详情
		try {
			MailUtils.send(session, mail);// 发送邮件
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		request.setAttribute("msg", "恭喜，注册成功！请马上到邮箱激活");
		return "f:/jsps/msg.jsp";
	}
}

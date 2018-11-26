package cn.itcast.bookstore.user.web.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.vcode.utils.VerifyCode;

public class VerifyCodeServlet extends HttpServlet {
	/**
	 *验证码
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取验证码对象
		VerifyCode vr =new VerifyCode();
		//生成图片
		BufferedImage image =vr.getImage();
		//保存验证码
		request.getSession().setAttribute("vcode_session", vr.getText());
		//使用response回传照片数据
		VerifyCode.output(image, response.getOutputStream());
	}

}

package cn.itcast.bookstore.user.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.bookstore.user.domain.User;

/**
 * Servlet Filter implementation class LoginFilter
 */
public class LoginFilter implements Filter {

    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}
	//过滤购物车和订单信息
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httprequest =(HttpServletRequest) request;
		User user=(User)httprequest.getSession().getAttribute("user_session");
		if(user!=null){
			chain.doFilter(request, response);
		}else {
			httprequest.setAttribute("msg", "您还未登录！");
			httprequest.getRequestDispatcher("/jsps/user/login.jsp").forward(httprequest, response);
		}
		
	}


	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

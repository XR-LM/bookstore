package cn.itcast.bookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.bookstore.cart.domain.Cart;
import cn.itcast.bookstore.cart.domain.CartItem;
import cn.itcast.bookstore.order.domain.Order;
import cn.itcast.bookstore.order.domain.OrderItem;
import cn.itcast.bookstore.order.service.OrderException;
import cn.itcast.bookstore.order.service.OrderService;
import cn.itcast.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	//支付订单
	public String pay(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		//设置参数
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("merchantInfo.properties");
		props.load(input);
		/*
		 * 准备13参数
		 */
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = request.getParameter("oid");
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";

		/*
		 * 计算hmac
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);

		/*
		 * 连接易宝的网址和13+1个参数
		 */
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);

		System.out.println(url);

		/*
		 * 重定向到易宝
		 */
		response.sendRedirect(url.toString());

		return null;
	}
	/**
	 * 这个方法是易宝回调方法 我们必须要判断调用本方法的是不是易宝！
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 获取11 + 1
		 */
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");

		String hmac = request.getParameter("hmac");

		/*
		 * 2. 校验访问者是否为易宝！
		 */
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("merchantInfo.properties");
		props.load(input);
		String keyValue = props.getProperty("keyValue");

		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		
		if(!bool) {//如果校验失败
			request.setAttribute("msg", "信息有误！");
			return "f:/jsps/msg.jsp";
		}
		
		/*
		 * 3. 获取状态订单，确定是否要修改订单状态，以及添加积分等业务操作
		 */
		orderService.zhifu(r6_Order);//有可能对数据库进行操作，也可能不操作！
		
		/*
		 * 4. 判断当前回调方式
		 *   如果为点对点，需要回馈以success开头的字符串
		 */
		if(r9_BType.equals("2")) {
			response.getWriter().print("success");
		}
		
		/*
		 * 5. 保存成功信息，转发到msg.jsp
		 */
		request.setAttribute("msg", "支付成功！等待卖家发货！你慢慢等~");
		return "f:/jsps/msg.jsp";
	}
	//生成订单
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1、从session中获取购物车
		 * 2、从session中获取user对象
		 * 3、完成订单条目类
		 * 4、将订单条目添加到订单类，并完成订单类的其他内容
		 * 5、将订单保存到session中
		 * 6、将页面转发到/jsps/order/list.jsp
		 *
		 */
		//获取购物车
		Cart cart =(Cart)request.getSession().getAttribute("cart");
		//获取用户
		User user =(User)request.getSession().getAttribute("user_session");
		//获取订单类设置相关属性
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(1);//初始状态为未支付
		order.setTotal(cart.getTotal());
		order.setUser(user);
		//设置购物条目
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (CartItem cartItem:cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();//创建订单条目
			orderItem.setIid(CommonUtils.uuid());//设置订单编号
			orderItem.setCount(cartItem.getCount());//设置商品数量
			orderItem.setBook(cartItem.getBook());//设置图书详情
			orderItem.setSubtotal(cartItem.getSubtotal());//总金额
			orderItem.setOrder(order);//添加到订单中
			orderItemList.add(orderItem);//将订单条目添加到购物条目中
		}
		order.setOrderItemList(orderItemList);
		// 清空购物车
		cart.clear();
		
		/*
		 * 3. 调用orderService添加订单
		 */
		orderService.add(order);
		/*
		 * 4. 保存order到request域，转发到/jsps/order/desc.jsp
		 */
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
	//查询我的订单
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取用户
		User user =(User)request.getSession().getAttribute("user_session");
		String uidString =user.getUid();
		List<Order> orderList =orderService.myOrder(uidString);//获取订单列表
		request.setAttribute("myorders", orderList);	
		return "f:/jsps/order/list.jsp";
	}
	//付款
	/*
	 * 通过订单编号查询指定订单
	 */

	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//将查询的订单数据保存到request域中
		request.setAttribute("order", orderService.load(request.getParameter("oid")));
		//转发到付款页面
		return "f:/jsps/order/desc.jsp";
	}
	//收货
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oid = request.getParameter("oid");//获取订单编号
		try {
			orderService.confirm(oid);
			//收货成功
			request.setAttribute("msg", "订单交易成功！");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}
}

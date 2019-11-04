package tmall.servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.omg.CORBA.Request;
import org.springframework.web.util.HtmlUtils;

import tmall.bean.Category;
import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.bean.PropertyValue;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.dao.OrderDAO;
import tmall.dao.ProductImageDAO;
import tmall.util.Page;

public class ForeServlet extends BaseForeServlet {
	public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Category> cs = categoryDAO.list();
		productDAO.fill(cs);
		productDAO.fillByRow(cs);
		request.setAttribute("cs", cs);

		return "home.jsp";
	}

	public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = (String) request.getAttribute("name");
		String password = (String) request.getAttribute("password");
		name = HtmlUtils.htmlEscape(name);
		boolean exist = userDAO.isExist(name);
		if (exist) {

			request.setAttribute("msg", "”√ªß√˚÷ÿ∏¥");
			return "register.jsp";
		}
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		userDAO.add(user);
		return "@registerSuccess.jsp";
	}

	public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = request.getParameter("name");
		name = HtmlUtils.htmlEscape(name);
		String password = request.getParameter("password");

		User user = userDAO.get(name, password);
		if (null == user) {
			request.setAttribute("msg", "’À∫≈√‹¬Î¥ÌŒÛ");
			return "login.jsp";

		}

		request.getSession().setAttribute("user", user);
		return "@forehome";

	}

	public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
		request.getSession().removeAttribute("user");

		return "forehome";
	}

	public String product(HttpServletRequest request, HttpServletResponse response, Page page) {

		int pid = Integer.parseInt(request.getParameter("pid"));
		Product product = productDAO.get(pid);
		List<ProductImage> productSingleImages = productImageDAO.list(product, ProductImageDAO.type_single);
		List<ProductImage> productDetailImages = productImageDAO.list(product, productImageDAO.type_detail);

		product.setProductSingleImages(productSingleImages);
		product.setProductDetailImages(productDetailImages);

		List<PropertyValue> pvs = propertyValueDAO.list(pid);
		List<Review> reviews = reviewDAO.list(pid);

		productDAO.setSaleAndReviewNumber(product);
		request.setAttribute("p", product);

		request.setAttribute("reviews", reviews);

		request.setAttribute("pvs", pvs);

		return "product.jsp";
	}

	public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user = (User) request.getSession().getAttribute("user");
		if (null == user)
			return "%fail";

		return "%success";
	}

	public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		User user = userDAO.get(name, password);

		if (null == user) {
			return "%fail";
		}
		request.getSession().setAttribute("user", user);
		return "%success";
	}

	public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		int num = Integer.parseInt(request.getParameter("num"));
		boolean found = false;
		Product p = productDAO.get(pid);

		int oiid = 0;

		User user = (User) request.getSession().getAttribute("user");

		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == pid) {
				oi.setNumber(oi.getNumber() + num);
				orderItemDAO.update(oi);
				found = true;
				oiid = oi.getId();
				break;

			}

		}
		if (!found) {
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setNumber(num);
			oi.setProduct(p);
			orderItemDAO.add(oi);
			oiid = oi.getId();

		}
		return "@forebuy?oiid=" + oiid;
	}

	public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {

		String[] oiids = request.getParameterValues("oiid");
		List<OrderItem> ois = new ArrayList<OrderItem>();
		float total = 0;
		for (String strid : oiids) {

			OrderItem oi = orderItemDAO.get(Integer.parseInt(strid));
			total = oi.getNumber() * oi.getProduct().getPromotePrice();
			ois.add(oi);

		}

		request.getSession().setAttribute("ois", ois);
		request.setAttribute("total", total);

		return "@buy.jsp";
	}
	
	public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid=Integer.parseInt(request.getParameter("pid"));
		Product p=productDAO.get(pid);
		int num=Integer.parseInt(request.getParameter("num"));
		
		User user=(User) request.getSession().getAttribute("user");
		boolean found=false;
		List<OrderItem> ois=orderItemDAO.listByUser(user.getId());
		
		for(OrderItem oi:ois) {
			
			if(p.getId()==oi.getProduct().getId()) {
				
				oi.setNumber(oi.getNumber()+num);
				orderItemDAO.update(oi);
                found = true;
                break;
			}
			
			
		}
		if(!found){
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setNumber(num);
            oi.setProduct(p);
            orderItemDAO.add(oi);
        }
        return "%success";
		
		
		
	}
	public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
	    User user =(User) request.getSession().getAttribute("user");
	    List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
	    request.setAttribute("ois", ois);
	    return "cart.jsp";
	} 
	public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
	    User user =(User) request.getSession().getAttribute("user");
	    if(null==user)
	        return "%fail";
	 
	    int pid = Integer.parseInt(request.getParameter("pid"));
	    int number = Integer.parseInt(request.getParameter("number"));
	    List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
	    for (OrderItem oi : ois) {
	        if(oi.getProduct().getId()==pid){
	            oi.setNumber(number);
	            orderItemDAO.update(oi);
	            break;
	        }
	         
	    }      
	    return "%success";
	}
	public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page){
	    User user =(User) request.getSession().getAttribute("user");
	    if(null==user)
	        return "%fail";
	    int oiid = Integer.parseInt(request.getParameter("oiid"));
	    orderItemDAO.delete(oiid);
	    return "%success";
	}
	
	public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
		User user =(User) request.getSession().getAttribute("user");
		
		List<OrderItem> ois= (List<OrderItem>) request.getSession().getAttribute("ois");
		if(ois.isEmpty())
			return "@login.jsp";

		String address = request.getParameter("address");
		String post = request.getParameter("post");
		String receiver = request.getParameter("receiver");
		String mobile = request.getParameter("mobile");
		String userMessage = request.getParameter("userMessage");
		
		Order order = new Order();
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +RandomUtils.nextInt(10000);

		order.setOrderCode(orderCode);
		order.setAddress(address);
		order.setPost(post);
		order.setReceiver(receiver);
		order.setMobile(mobile);
		order.setUserMessage(userMessage);
		order.setCreateDate(new Date());
		order.setUser(user);
		order.setStatus(OrderDAO.waitPay);

		orderDAO.add(order);
		float total =0;
		for (OrderItem oi: ois) {
			oi.setOrder(order);
			orderItemDAO.update(oi);
			total+=oi.getProduct().getPromotePrice()*oi.getNumber();
		}
		
		return "@forealipay?oid="+order.getId() +"&total="+total;
	}
	public String alipay(HttpServletRequest request, HttpServletResponse response, Page page){
	    return "alipay.jsp";
	}
	public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
	    int oid = Integer.parseInt(request.getParameter("oid"));
	    Order order = orderDAO.get(oid);
	    order.setStatus(OrderDAO.waitDelivery);
	    order.setPayDate(new Date());
	    new OrderDAO().update(order);
	    request.setAttribute("o", order);
	    return "payed.jsp";    
	} 
	public String bought(HttpServletRequest request, HttpServletResponse response, Page page) {
	    User user =(User) request.getSession().getAttribute("user");
	    List<Order> os= orderDAO.list(user.getId(),OrderDAO.delete);
	     
	    orderItemDAO.fill(os);
	     
	    request.setAttribute("os", os);
	     
	    return "bought.jsp";       
	}
	public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
	    int oid = Integer.parseInt(request.getParameter("oid"));
	    Order o = orderDAO.get(oid);
	    orderItemDAO.fill(o);
	    request.setAttribute("o", o);
	    return "confirmPay.jsp";       
	}
	public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page) {
	    int oid = Integer.parseInt(request.getParameter("oid"));
	    Order o = orderDAO.get(oid);
	    o.setStatus(OrderDAO.waitReview);
	    o.setConfirmDate(new Date());
	    orderDAO.update(o);
	    return "orderConfirmed.jsp";
	}
	public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page){
	    int oid = Integer.parseInt(request.getParameter("oid"));
	    Order o = orderDAO.get(oid);
	    o.setStatus(OrderDAO.delete);
	    orderDAO.update(o);
	    return "%success";     
	}
	public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
	    int oid = Integer.parseInt(request.getParameter("oid"));
	    Order o = orderDAO.get(oid);
	    orderItemDAO.fill(o);
	    Product p = o.getOrderItems().get(0).getProduct();
	    List<Review> reviews = reviewDAO.list(p.getId());
	    productDAO.setSaleAndReviewNumber(p);
	    request.setAttribute("p", p);
	    request.setAttribute("o", o);
	    request.setAttribute("reviews", reviews);
	    return "review.jsp";       
	}
	public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) {
	    int oid = Integer.parseInt(request.getParameter("oid"));
	    Order o = orderDAO.get(oid);
	    o.setStatus(OrderDAO.finish);
	    orderDAO.update(o);
	    int pid = Integer.parseInt(request.getParameter("pid"));
	    Product p = productDAO.get(pid);
	     
	    String content = request.getParameter("content");
	     
	    content = HtmlUtils.htmlEscape(content);
	 
	    User user =(User) request.getSession().getAttribute("user");
	    Review review = new Review();
	    review.setContent(content);
	    review.setProduct(p);
	    review.setCreateDate(new Date());
	    review.setUser(user);
	    reviewDAO.add(review);
	     
	    return "@forereview?oid="+oid+"&showonly=true";    
	}
	

}




























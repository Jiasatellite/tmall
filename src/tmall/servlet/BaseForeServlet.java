package tmall.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.dao.CategoryDAO;
import tmall.dao.OrderDAO;
import tmall.dao.OrderItemDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.dao.PropertyDAO;
import tmall.dao.PropertyValueDAO;
import tmall.dao.ReviewDAO;
import tmall.dao.UserDAO;
import tmall.util.Page;

public class BaseForeServlet extends HttpServlet {

	protected CategoryDAO categoryDAO = new CategoryDAO();
	protected OrderDAO orderDAO = new OrderDAO();
	protected OrderItemDAO orderItemDAO = new OrderItemDAO();
	protected ProductDAO productDAO = new ProductDAO();
	protected ProductImageDAO productImageDAO = new ProductImageDAO();
	protected PropertyDAO propertyDAO = new PropertyDAO();
	protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
	protected ReviewDAO reviewDAO = new ReviewDAO();
	protected UserDAO userDAO = new UserDAO();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			int start=0;
			int count=10;
			try {
				start=Integer.parseInt(request.getParameter("page.start"));
				
				
			}catch (Exception e) {
				e.printStackTrace();// TODO: handle exception
			}
			try {
				count=Integer.parseInt(request.getParameter("page.count"));
				
				
			}catch (Exception e) {
				e.printStackTrace();// TODO: handle exception
			}
			Page page=new Page(start, count);
			String method=(String) request.getAttribute("method");
			Method m=this.getClass().getMethod(method, HttpServletRequest.class,HttpServletResponse.class,Page.class);
			String redirect=m.invoke(this, request,response,page).toString();
			if(redirect.startsWith("@")) {
				response.sendRedirect(redirect.substring(1));
			}
			if(redirect.startsWith("%"))
				response.getWriter().print(redirect.substring(1));
			else
				request.getRequestDispatcher(redirect).forward(request, response);
		}catch (Exception e) {
			
			e.printStackTrace();// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
}





















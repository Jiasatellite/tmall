package tmall.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.dao.CategoryDAO;
import tmall.util.Page;

public class PropertyServlet extends BaseBackServlet {

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int cid=Integer.parseInt(request.getParameter("cid"));
		Category c=categoryDAO.get(cid);
		String name=request.getParameter("name");
		Property p=new Property();
		p.setCategory(c);
		p.setName(name);
		propertyDAO.add(p);
		return "@admin_property_list?cid="+p.getCategory().getId();
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
	    Property p = propertyDAO.get(id);
	    propertyDAO.delete(id);
	    return "@admin_property_list?cid="+p.getCategory().getId();
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int  id=Integer.parseInt(request.getParameter("id"));
		Property p=propertyDAO.get(id);
		request.setAttribute("p", p);
		
		
		return "admin/editProperty.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int cid=Integer.parseInt(request.getParameter("cid"));
		Category c=categoryDAO.get(cid);
		
		int id=Integer.parseInt(request.getParameter("id"));
		String name=request.getParameter("name");
		Property p=new Property();
		p.setName(name);
		p.setCategory(c);
		p.setId(id);
		propertyDAO.update(p);
		return "@admin_property_list?cid="+p.getCategory().getId();
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		List<Property> ps = new ArrayList<>();
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);
		ps = propertyDAO.list(cid, page.getStart(), page.getCount());
		int total = productDAO.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid=" + c.getId());/////////////////////////////////////////////////////////
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		request.setAttribute("ps", ps);
		return "admin/listProperty.jsp";
	}

}

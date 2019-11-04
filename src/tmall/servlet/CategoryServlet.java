package tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class CategoryServlet extends BaseBackServlet {

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		InputStream is = super.parseUpload(request, params);
		String name = params.get("name");
		Category category = new Category();
		category.setName(name);
		categoryDAO.add(category);// id“—ÃÌº”

		File imageFolder = new File(request.getSession().getServletContext().getRealPath("/img/category"));
		File f = new File(imageFolder, category.getId() + ".jpg");
		try {
			if (null != is && 0 != is.available()) {
				try (FileOutputStream fos = new FileOutputStream(f);) {
					byte[] b = new byte[1024 * 1024];
					int length = 0;
					while (-1 != (length = is.read(b))) {
						fos.write(b, 0, length);

					}
					fos.flush();
					BufferedImage img = ImageUtil.change2jpg(f);
					ImageIO.write(img, "jpg", f);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "@admin_category_list";
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		categoryDAO.delete(id);
		return "@admin_category_list";
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		Category category = categoryDAO.get(id);
		request.setAttribute("c", category);
		return "admin/editCategory.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		Map<String,String> params=new HashMap<>();
		InputStream is=super.parseUpload(request, params);
		int id=Integer.parseInt(params.get("id"));
		String name=params.get("name");
		Category c=new Category();
		c.setId(id);
		c.setName(name);
		File imagefolder=new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file=new File(imagefolder,id+".jpg");
		file.getParentFile().mkdirs();
		try {		
			if(null!=is&&0!=is.available()) {
				try(FileOutputStream fos=new FileOutputStream(file)){
					byte[] b=new byte[1024*1024];
					int length=0;
					while(-1!=(length=is.read(b))) {
						fos.write(b,0,length);
						
					}
					fos.flush();
					BufferedImage img=ImageUtil.change2jpg(file);
					ImageIO.write(img, "jpg", file);
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
			}
		
		
		
		}catch(Exception e) {e.printStackTrace();}
		
		return "@admin_category_list";
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		List<Category> cs = categoryDAO.list(page.getStart(), page.getCount());
		int total = categoryDAO.getTotal();
		page.setTotal(total);
		request.setAttribute("thecs", cs);
		request.setAttribute("page", page);

		return "/admin/listCategory.jsp";
	}

}

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

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.ProductImageDAO;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class ProductImageServlet extends BaseBackServlet {

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		InputStream is = null;
		Map<String, String> params = new HashMap<String, String>();
		is = parseUpload(request, params);
		String type = params.get("type");
		int pid = Integer.parseInt(params.get("pid"));
		Product p = productDAO.get(pid);
		ProductImage pi = new ProductImage();
		pi.setProduct(p);
		pi.setType(type);
		productImageDAO.add(pi);
		String filename = pi.getId() + ".jpg";
		String imageFolder;
		String imageFolder_small = null;
		String imageFolder_middle = null;
		if (ProductImageDAO.type_single.equals(pi.getType())) {
			imageFolder = request.getSession().getServletContext().getRealPath("img/productSingle");
			imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
			imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
		} else
			imageFolder = request.getSession().getServletContext().getRealPath("img/productDetail");
		File f = new File(imageFolder, filename);
		f.getParentFile().mkdirs();
		try (FileOutputStream fos = new FileOutputStream(f);) {
			byte[] b = new byte[1024 * 1024];
			if (null != is && 0 != is.available()) {
				int length = 0;
				while (-1 != (length = is.read(b))) {
					fos.write(b, 0, length);

				}
				fos.flush();
				BufferedImage img = ImageUtil.change2jpg(f);
				ImageIO.write(img, "jpg", f);
				if (ProductImageDAO.type_single.equals(pi.getType())) {
					File f_small = new File(imageFolder_small, filename);
					File f_middle = new File(imageFolder_middle, filename);

					ImageUtil.resizeImage(f, 56, 56, f_small);
					ImageUtil.resizeImage(f, 217, 190, f_middle);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "@admin__productImage_list?pid=" + pid;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int id=Integer.parseInt(request.getParameter("id"));
		ProductImage pi=productImageDAO.get(id);
		productImageDAO.delete(id);
		if(ProductImageDAO.type_single.equals(pi.getType())) {
			String single=request.getSession().getServletContext().getRealPath("img/productSingle");
			String small= request.getSession().getServletContext().getRealPath("img/productSingle_small");
			String middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
			File single_=new File(single,pi.getId()+".jpg");
			single_.delete();
			File small_=new File(small,pi.getId()+".jpg");
			small_.delete();
			
			File middle_=new File(middle,pi.getId()+".jpg");
			middle_.delete();
		}
		else
		{
			
			
			String detail=request.getSession().getServletContext().getRealPath("img/productDetail");
			File detail_=new File(detail,pi.getId()+".jpg");
			detail_.delete();
		}
		return "admin_productImage_list?pid="+pi.getProduct().getId();
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		List<ProductImage> singlepis = productImageDAO.list(p, ProductImageDAO.type_single, page.getStart(),
				page.getCount());
		List<ProductImage> detailpis = productImageDAO.list(p, ProductImageDAO.type_detail, page.getStart(),
				page.getCount());
		request.setAttribute("pisSingle", singlepis);
		request.setAttribute("pisDetail", detailpis);
		request.setAttribute("p", p);
		return "admin/listProductImage.jsp";
	}

}

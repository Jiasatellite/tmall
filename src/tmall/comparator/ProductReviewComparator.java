package tmall.comparator;

import java.util.Comparator;

import tmall.bean.Product;

public class ProductReviewComparator implements Comparator<Product>{

	@Override
	public int compare(Product o1, Product o2) {
		// TODO Auto-generated method stub
		return o2.getReviewCount()-o1.getReviewCount();
	}

}

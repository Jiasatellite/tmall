package tmall.util;

public class Page {
	int start;
	int count;
	int total;
	String param;
public Page(int start,int count) {
	super();
	this.start=start;
	this.count=count;
}
public boolean isHasPreviouse() {
	if(start==0)
		return false;
	return true;	
}
public int getLast() {
	int last;
	if(0==total%count)
		last = total - count;
	else 
		last = total - total % count;
	last = last<0?0:last;
    return last;
}
public boolean isHasNext() {
	if(start==getLast())
		return false;
	return true;	
}
public int getTotalPage() {
	int totalpage;
	if(0==total%count)
		totalpage=total/count;
	else 
		totalpage=total/count+1;
	if(0==totalpage)
		 totalpage=1;
	return totalpage;	
}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}

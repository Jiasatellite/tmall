package tmall.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;

public class PropertyDAO {
	public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = (Statement) c.createStatement();) {
  
            String sql = "select count(*) from Property where cid =" + cid;
  
            ResultSet rs = (ResultSet) s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return total;
    }
  
    public void add(Property bean) {
 
        String sql = "insert into Property values(null,?,?)";
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql);) {
  
            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
            ps.execute();
  
            ResultSet rs = (ResultSet) ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public void update(Property bean) {
 
        String sql = "update Property set cid= ?, name=? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql);) {
 
            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
            ps.setInt(3, bean.getId());
            ps.execute();
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
  
    }
  
    public void delete(int id) {
  
        try (Connection c = DBUtil.getConnection(); Statement s = (Statement) c.createStatement();) {
  
            String sql = "delete from Property where id = " + id;
  
            s.execute(sql);
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public Property get(int id) {
        Property bean = new Property();
  
        try (Connection c = DBUtil.getConnection(); Statement s = (Statement) c.createStatement();) {
  
            String sql = "select * from Property where id = " + id;
  
            ResultSet rs = (ResultSet) s.executeQuery(sql);
  
            if (rs.next()) {
 
                String name = rs.getString("name");
                int cid = rs.getInt("cid");
                bean.setName(name);
                Category category = new CategoryDAO().get(cid);
                bean.setCategory(category);
                bean.setId(id);
            }
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return bean;
    }
  
    public List<Property> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }
  
    public List<Property> list(int cid, int start, int count) {
        List<Property> beans = new ArrayList<Property>();
  
        String sql = "select * from Property where cid = ? order by id desc limit ?,? ";
  
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql);) {
  
            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);
  
            ResultSet rs = (ResultSet) ps.executeQuery();
  
            while (rs.next()) {
                Property bean = new Property();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                bean.setName(name);
                Category category = new CategoryDAO().get(cid);
                bean.setCategory(category);
                bean.setId(id);
                 
                beans.add(bean);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return beans;
    }
}

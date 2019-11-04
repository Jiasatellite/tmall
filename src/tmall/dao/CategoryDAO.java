package tmall.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import tmall.bean.Category;
import tmall.util.DBUtil;

public class CategoryDAO {
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = (Statement) c.createStatement();) {
			String sql = "select count(*) from category";
			ResultSet rs = (ResultSet) s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return total;
	}

	public void add(Category bean) {
		String sql = "insert into category values(null,?)";
		try (Connection c = DBUtil.getConnection(); Statement s = (Statement) c.createStatement();) {
			s.execute(sql);
			ResultSet rs = (ResultSet) s.getGeneratedKeys();
			while (rs.next()) {
				bean.setId(rs.getInt(1));
			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void update(Category bean) {
		String sql = "update category set name=? where id=?";
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql);) {
			ps.setString(1, bean.getName());
			ps.setInt(2, bean.getId());
			ps.execute();

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = (Statement) c.createStatement();) {
			String sql = "delete from category where id=" + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public Category get(int id) {
		Category bean = null;
		try (Connection c = DBUtil.getConnection(); Statement s = (Statement) c.createStatement();) {
			String sql = "slect from category where id=" + id;
			ResultSet rs = (ResultSet) s.executeQuery(sql);
			while (rs.next()) {
				bean = new Category();
				bean.setName(rs.getString("name"));
				bean.setId(id);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<Category> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<Category> list(int start, int count) {
		String sql = "select from category order by id desc limit ?,?";
		List<Category> categorys = new ArrayList<>();
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql);) {
			ps.setInt(1, start);
			ps.setInt(2, count);
			ResultSet rs = (ResultSet) ps.executeQuery();
			while (rs.next()) {
				Category bean = new Category();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString("name"));
				categorys.add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return categorys;
	}
}

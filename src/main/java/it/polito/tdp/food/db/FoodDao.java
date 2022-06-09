package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public void listAllFoods(Map<Integer, Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				try {
					if(!idMap.containsKey(res.getInt("food_code"))) {
						idMap.put(res.getInt("food_code"), new Food(res.getInt("food_code"),res.getString("display_name")));
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Food> getVertici(int porzioni, Map<Integer, Food> idMap){
		String sql = "SELECT food_code "
				+ "FROM `portion` "
				+ "GROUP BY food_code "
				+ "HAVING COUNT(*) <= ? AND COUNT(*) > 0;";
		List<Food> vertici = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				Food f = idMap.get(res.getInt("food_code"));
				if(f != null) {
					vertici.add(f);
				}
			}
			conn.close();
			return vertici;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Food> idMap){
		String sql = "SELECT f1.food_code AS f1c, f2.food_code AS f2c, AVG(cond.condiment_calories) AS peso "
				+ "FROM food_condiment AS f1, food_condiment AS f2, condiment AS cond "
				+ "WHERE f1.id <> f2.id AND f1.condiment_code = cond.condiment_code AND f1.condiment_code = f2.condiment_code "
				+ "GROUP BY f1.food_code, f2.food_code";
		List<Adiacenza> archi = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				Food f1 = idMap.get(res.getInt("f1c"));
				Food f2 = idMap.get(res.getInt("f2c"));
				if(f1 != null && f2 != null && !f1.equals(f2)) {
					archi.add(new Adiacenza(f1, f2, res.getDouble("peso")));
				}
			}
			conn.close();
			return archi;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Double getCalorieCongiunte(Food f1, Food f2) {
		String sql = "SELECT f1.food_code , f2.food_code , AVG(cond.condiment_calories) AS peso"
				+ "FROM food_condiment AS f1, food_condiment AS f2, condiment AS cond "
				+ "WHERE f1.id <> f2.id AND f1.condiment_code = cond.condiment_code AND f1.condiment_code = f2.condiment_code AND f1.food_code = ? AND f2.food_code = ? "
				+ "GROUP BY f1.food_code, f2.food_code";
		Double d = null;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, f1.getFood_code());
			st.setInt(2, f2.getFood_code());
			ResultSet res = st.executeQuery() ;
			if(res.first()) {
				d = res.getDouble("peso");
			}
			conn.close();
			return d;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

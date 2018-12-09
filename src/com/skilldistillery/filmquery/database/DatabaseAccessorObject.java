package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String url = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";

	public DatabaseAccessorObject() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film getFilmById(int filmId) {

		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT title, description, release_year, language_id, rental_duration, "
			+ " rental_rate, length, replacement_cost, rating, special_features FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String title = rs.getString(1);
				String desc = rs.getString(2);
				int releaseYear = rs.getInt(3);
				int langId = rs.getInt(4);
				int rentDur = rs.getInt(5);
				double rate = rs.getDouble(6);
				int length = rs.getInt(7);
				double repCost = rs.getDouble(8);
				String rating = rs.getString(9);
				String features = rs.getString(10);
				film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features, getActorsByFilmId(filmId), getLanguage(langId), getCategory(filmId));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor getActorById(int actorId) {

		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				String first_name = actorResult.getString(1);
				String last_name = actorResult.getString(2);
				actor = new Actor(actorId, first_name, last_name); // Create the object

				actorResult.close();
				stmt.close();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;

	}

	@Override
	public List<Actor> getActorsByFilmId(int filmId) {
		List<Actor> actorlist = new ArrayList<>();
		
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT a.id FROM film f join film_actor fa on f.id  = "
					+ "fa.film_id join actor a on fa.actor_id = a.id where f.id = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				
				actorlist.add(getActorById(rs.getInt(1)));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return actorlist;
	}

	@Override
	public List<Film> getFilmsByKeyword(String keyword) {
		List<Film> filmList = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "select id, title from film where title like ? or description like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				filmList.add(getFilmById(rs.getInt(1)));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return filmList;
	}

	@Override
	public String getLanguage(int langId) {
		String lang = "";
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "select name from language where id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, langId);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				lang = rs.getString(1);
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lang;
	}

	@Override
	public String getCategory(int filmId) {
		String category = "";
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "select name from category cat join film_category fmcat on cat.id = "
					+ "fmcat.category_id join film fm on fmcat.film_id = fm.id where fm.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				category = rs.getString(1);
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return category;
	}

	@Override
	public List<String> getInventoryRaw(int filmId) {
		List<String> inventoryList = new ArrayList<>();
		
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "select inv.film_id, inv. media_condition, inv.store_id, ad.city,"
					+ " ad.state_province from inventory_item inv join store st on inv.store_id "
					+ "= st.id join address ad on st.address_id = ad.id where inv.film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				StringBuilder invData = new StringBuilder();
				invData.append("Film ID: ");
				invData.append(rs.getString(1));
				invData.append(" Condition: ");
				invData.append(rs.getString(2));
				invData.append(" At: Store Id: ");
				invData.append(rs.getString(3));
				invData.append(" City: ");
				invData.append(rs.getString(4));
				invData.append(" State: ");
				invData.append(rs.getString(5));
				inventoryList.add(invData.toString());
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return inventoryList;
	}

	@Override
	public String getInventoryUsed(int filmId) {
		StringBuilder inventory = new StringBuilder();
		StringBuilder usedFullString = new StringBuilder();
		List<String> rawInventory = getInventoryRaw(filmId);
		Set<String> usedInventoryLocationsSet = new HashSet<>();

		String regexUsed = "(Used[\\s*\\w*:]{1,})";
		Pattern patternUsed = Pattern.compile(regexUsed, Pattern.MULTILINE);
		Matcher matcherUsed = patternUsed.matcher(rawInventory.toString());

		int used = 0;
		while (matcherUsed.find()) {
			usedFullString.append(matcherUsed.group() + ", ");
			usedInventoryLocationsSet.add(matcherUsed.group());
		    ++used;
		}
		
		List<String> usedInventoryLocationList = new ArrayList<>(usedInventoryLocationsSet);
		
		for (int i = 0; i < usedInventoryLocationList.size(); i++) {
			String regexLocation = usedInventoryLocationList.get(i);
			
			 
			Pattern patternLocation = Pattern.compile(regexLocation, Pattern.MULTILINE);
			Matcher matcherLocation = patternLocation.matcher(usedFullString.toString());
			
			int numInLocation = 0;
			while (matcherLocation.find()) {
				
			    ++numInLocation;
			}
			
			usedInventoryLocationList.set(i, 
					numInLocation + " " + usedInventoryLocationList.get(i));
		}
		inventory.append(used + " Total used. Availability: " + usedInventoryLocationList.toString());
		
		if(used == 0){
			return null;
		}
		return inventory.toString();
	}

	@Override
	public String getInventoryNew(int filmId) {
		StringBuilder inventory = new StringBuilder();
		StringBuilder newFullString = new StringBuilder();
		List<String> rawInventory = getInventoryRaw(filmId);
		Set<String> newInventoryLocationsSet = new HashSet<>();

		String regexNew = "(New[\\s*\\w*:]{1,})";
		Pattern patternNew = Pattern.compile(regexNew, Pattern.MULTILINE);
		Matcher matcherNew = patternNew.matcher(rawInventory.toString());

		int total = 0;
		while (matcherNew.find()) {
			newFullString.append(matcherNew.group() + ", ");
			newInventoryLocationsSet.add(matcherNew.group());
		    ++total;
		}
		
		List<String> newInventoryLocationList = new ArrayList<>(newInventoryLocationsSet);
		
		for (int i = 0; i < newInventoryLocationList.size(); i++) {
			String regexLocation = newInventoryLocationList.get(i);
			
			 
			Pattern patternLocation = Pattern.compile(regexLocation, Pattern.MULTILINE);
			Matcher matcherLocation = patternLocation.matcher(newFullString.toString());
			
			int numInLocation = 0;
			while (matcherLocation.find()) {
				
			    ++numInLocation;
			}
			
			newInventoryLocationList.set(i, 
					numInLocation + " " + newInventoryLocationList.get(i));
		}
		inventory.append(total + " Total new. Availability: " + newInventoryLocationList.toString());
		
		if(total == 0){
			return null;
		}
		return inventory.toString();
	}

	@Override
	public String getInventoryDamaged(int filmId) {
		StringBuilder inventory = new StringBuilder();
		StringBuilder damamgedFullString = new StringBuilder();
		List<String> rawInventory = getInventoryRaw(filmId);
		Set<String> damamgedInventoryLocationsSet = new HashSet<>();
		//gets me the raw data for only damaged video and the location
		String regexDamamged = "(Damaged[\\s*\\w*:]{1,})";
		Pattern patternDamamged = Pattern.compile(regexDamamged, Pattern.MULTILINE);
		Matcher matcherDamamged = patternDamamged.matcher(rawInventory.toString());

		int total = 0;
		while (matcherDamamged.find()) {
			damamgedFullString.append(matcherDamamged.group() + ", ");
			damamgedInventoryLocationsSet.add(matcherDamamged.group());
		    ++total;
		}
		
		List<String> damamgedInventoryLocationList = new ArrayList<>(damamgedInventoryLocationsSet);
		//to get the individual number at each location
		for (int i = 0; i < damamgedInventoryLocationList.size(); i++) {
			String regexLocation= damamgedInventoryLocationList.get(i);

			Pattern patternLocation = Pattern.compile(regexLocation, Pattern.MULTILINE);
			Matcher matcherLocation = patternLocation.matcher(damamgedFullString.toString());
			
			int numInLocation = 0;
			while (matcherLocation.find()) {
				
			    ++numInLocation;
			}
			
			damamgedInventoryLocationList.set(i, 
					numInLocation + " " + damamgedInventoryLocationList.get(i));
		}
		inventory.append(total + " Total damaged. Location: " + damamgedInventoryLocationList.toString());
		
		if(total == 0){
			return null;
		}
		return inventory.toString();
	}

	@Override
	public String getInventoryLost(int filmId) {
		StringBuilder inventory = new StringBuilder();
		StringBuilder lostFullString = new StringBuilder();
		List<String> rawInventory = getInventoryRaw(filmId);
		//using a set to get rid of duplicates 
		Set<String> lostInventoryLocationsSet = new HashSet<>();
		
		//this will give me the data for only lost videos
		String regexLost = "(Lost[\\s*\\w*:]{1,})";
		Pattern patternLost = Pattern.compile(regexLost, Pattern.MULTILINE);
		Matcher matcherLost = patternLost.matcher(rawInventory.toString());

		int total = 0;
		while (matcherLost.find()) {
			lostFullString.append(matcherLost.group()+ ", ");
			lostInventoryLocationsSet.add(matcherLost.group());
		    ++total;
		}
		
		List<String> lostInventoryLocationList = new ArrayList<>(lostInventoryLocationsSet);
		
		//this for loop gives me the number of videos at each location individually.
		for (int i = 0; i < lostInventoryLocationList.size(); i++) {
			String regexLocation= lostInventoryLocationList.get(i);
			
			Pattern patternLocation = Pattern.compile(regexLocation, Pattern.MULTILINE);
			Matcher matcherLocation = patternLocation.matcher(lostFullString.toString());
			
			int numInLocation = 0;
			while (matcherLocation.find()) {
				
			    ++numInLocation;
			}
			
			lostInventoryLocationList.set(i, 
					numInLocation + " " + lostInventoryLocationList.get(i));
		}
		inventory.append(total + " Total Lost. Location: " + lostInventoryLocationList.toString());
		if(total == 0){
			return null;
		}
		return inventory.toString();
	}

	@Override
	public void printInventory(int filmId) {
		
		if(getInventoryNew(filmId) != null) {
			System.out.println(getInventoryNew(filmId));
		}
		if(getInventoryUsed(filmId) != null) {
			System.out.println(getInventoryUsed(filmId));
		}
		if(getInventoryDamaged(filmId) != null) {
			System.out.println(getInventoryDamaged(filmId));
		}
		if(getInventoryLost(filmId) != null) {
			System.out.println(getInventoryLost(filmId));
		}
		
	}

}

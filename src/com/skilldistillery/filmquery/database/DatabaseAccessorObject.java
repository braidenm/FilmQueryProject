package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Film getFilmById(int filmId) {

		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT title, description, release_year, language_id, rental_duration, ";
			sql += " rental_rate, length, replacement_cost, rating, special_features " + " FROM film WHERE id = ?";
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
						features);
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
//	    actor.setFilms(getFilmsByActorId(actorId)); 
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
			String sql = "SELECT a.id, a.first_name, a.last_name FROM"
					+ " film f join film_actor fa on f.id  = "
					+ "fa.film_id join actor a on fa.actor_id = a.id where f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				
				int id = rs.getInt(1);
				String first_name = rs.getString(2);
				String last_name = rs.getString(3);
				Actor actor = new Actor(id, first_name, last_name);
				
				actorlist.add(actor);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return actorlist;
	}

}

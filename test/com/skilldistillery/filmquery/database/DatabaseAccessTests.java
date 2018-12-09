package com.skilldistillery.filmquery.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.skilldistillery.filmquery.entities.Film;

class DatabaseAccessTests {
  private DatabaseAccessor db;

  @BeforeEach
  void setUp() throws Exception {
    db = new DatabaseAccessorObject();
  }

  @AfterEach
  void tearDown() throws Exception {
    db = null;
  }

  @Test
  void test_getFilmById_with_invalid_id_returns_null() {
    Film f = db.getFilmById(-42);
    assertNull(f);
  }
  @Test
  @DisplayName("get film by id returns correct Film")
  void test_getFilmById(){
	  Film film = new Film(1, "ACADEMY DINOSAUR", "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies"
			  , 1993, 3, 6, 0.99, 86, 20.99, "PG", "Deleted Scenes,Behind the Scenes",
			  db.getActorsByFilmId(1), "Japanese", "Documentary");
	  Film checkedFilm = db.getFilmById(1);
	  
	  assertEquals(film, checkedFilm);
  }
  
  @Test
  @DisplayName("correct language is returned")
  void test_language() {
	String lang = db.getLanguage(db.getFilmById(1).getLanguage_id());
	  
	  assertEquals("Japanese", lang);
	  
  }
  @Test
  @DisplayName("get category returns correct result")
  void test_get_category() {
	 String cat = db.getCategory(1);
	 
	 assertEquals("Documentary", cat);
  }
  @Test
  @DisplayName("get film by keyword returns correct films")
  void test_getFilmByKeyword() {
	  
  }
  

}

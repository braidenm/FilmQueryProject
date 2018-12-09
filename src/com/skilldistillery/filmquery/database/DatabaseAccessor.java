package com.skilldistillery.filmquery.database;

import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public interface DatabaseAccessor {
  public Film getFilmById(int filmId);
  public Actor getActorById(int actorId);
  public List<Actor> getActorsByFilmId(int filmId);
  public List<Film> getFilmsByKeyword(String keyword);
  public String getLanguage(int langId);
  public String getCategory(int filmId);
  public List<String> getInventoryRaw(int filmId);
  public String getInventoryUsed(int filmId);
  public String getInventoryNew(int fimlId);
  public String getInventoryDamaged(int filmId);
  public String getInventoryLost(int filmId);
  public void printInventory(int filmId);
  
}

package com.skilldistillery.filmquery.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		app.launch();
	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		int choice = 0;
		DatabaseAccessorObject database = new DatabaseAccessorObject();

		boolean again = true;
		
		System.out.println("Welcome to the Film Query App.");
		System.out.println("Pick a choice to view films. when viewing films you will see");
		System.out.println("relevant data and have an option to view in mor detail");
		System.out.println("In options 1 and 2 you will also see inventory details.");
		System.out.println();
		System.out.println("_________________________");
		
		do {
			choice = menuSelect(choice, input);

			switch (choice) {
			case 1:
				again = option1(choice, input, database);
				break;
			case 2:
				again = option2(input, database);
				break;
			case 3: database.printAllMovies();
				again = true;
				break;
			case 4:
				System.out.println("Goodbye");
				System.exit(0);
			}

		} while (again);
		System.out.println("Goodbye");

	}

	private void menu() {
		System.out.println("1) Look up a film by it's ID (1-1000)");
		System.out.println("2) Look up a film by search keyword(s)");
		System.out.println("3) Print All Films");
		System.out.println("4) Exit the application");
		System.out.print("choice: ");
	}

	private int menuSelect(int choice, Scanner input) {
		do {
			try {
				System.out.println("What would you like to do?");
				menu();
				choice = input.nextInt();

				if (choice < 1 || choice > 3) {
					System.err.println(" Please enter a valid number 1 - 3 ");
					System.out.println();
					choice = 0;
				}
			} catch (Exception e) {
				input.nextLine();
				System.out.println();
				System.err.println(" Please enter a valid number 1 - 3: ");
				System.out.println();
				choice = 0;
			}

		} while (choice == 0);
		return choice;
	}

	private boolean option1(int choice, Scanner input, DatabaseAccessorObject database) {

		choice = 0;
		String again;
		do {
			again = "N";
			do {
				try {
					System.out.print("Enter a film id: ");
					choice = input.nextInt();

				} catch (Exception e) {
					input.next();
					System.err.println("Please enter a valid number");
					System.out.println();
					choice = 0;
				}
			} while (choice == 0);
			Film film = database.getFilmById(choice);
			if (film != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("Title: " + film.getTitle());
				sb.append(", Year: " + film.getYear());
				sb.append(", Rating: " + film.getRating());
				sb.append(", Language: " + film.getLanguage());
				sb.append(", Category: " + film.getCategory());
				sb.append(", Desctription: " + film.getDescription());
				System.out.println(sb);
				System.out.println(film.getCast());
				database.printInventory(film.getId());
				System.out.println();
				System.out.println("___________________");
			} else {
				System.err.print("Film not in directory. Try again? (Y/N): ");
				again = input.next();

			}
			if (again.equals("N")) {
				//could do a separate method for this submenu, but would need to make
				//a separate method for option2()'s submenu as well. one takes a film
				//and the other takes a list<Film> not to mention the need to
				//tie in the return statement would be more work than it would save.
				do {
					subMenu();
					choice = input.nextInt();
					System.out.println("____________________");
					System.out.println();

					if (choice == 2) {
						System.out.println(film);
						database.printInventory(film.getId());
						System.out.println();
						System.out.println("______________________");
						choice = 0;
					}
				} while (choice == 0);
			}
		} while (again.equalsIgnoreCase("Y") ? true : false);

		return choice == 1 ? true : false;
	}

	private boolean option2(Scanner input, DatabaseAccessorObject database) {
		List<Film> filmList = new ArrayList<>();
		String again;
		String phrase;
		do {
			input.nextLine();
			again = "N";
			System.out.print("Search phrase: ");
			phrase = input.nextLine();

			// if the user puts in more than one word
			phrase = phrase.replaceAll(" ", "% %");
			filmList = database.getFilmsByKeyword(phrase);

			if (filmList.size() == 0) {
				System.out.print("No films match. try again? (Y/N): ");
				again = input.next();
			}

		} while (again.equalsIgnoreCase("Y") ? true : false);
		
		int numFilms = 1;
		for (Film film : filmList) {
			StringBuilder sb = new StringBuilder();
			sb.append("Title: " + film.getTitle());
			sb.append(", Year: " + film.getYear());
			sb.append(", Rating: " + film.getRating());
			sb.append(", Language: " + film.getLanguage());
			sb.append(", Category: " + film.getCategory());
			sb.append(", Desctription: " + film.getDescription());
			
			System.out.println(sb);
			System.out.println(film.getCast());
			database.printInventory(film.getId());
			System.out.println();
			System.out.println("___________________");

			numFilms++;
		}
		System.out.println("number of films: " + numFilms);
		System.out.println("______________________");
		System.out.println();
		int choice = 0;
		//could do a separate method for this submenu, but would need to make
		//a separate method for option2()'s submenu as well. one takes a film
		//and the other takes a list<Film> not to mention the need to
		//tie in the return statement would be more work than it would save.
		do {
			subMenu();
			choice = input.nextInt();
			System.out.println("____________________");
			System.out.println();

			if (choice == 2) {
				for (Film film : filmList) {
					System.out.println(film);
					database.printInventory(film.getId());

					System.out.println();
				}
				System.out.println("______________________");
				choice = 0;
			}
		} while (choice == 0);

		return choice == 1 ? true : false;
	}

	private void subMenu() {
		System.out.println("1) return to main menu");
		System.out.println("2) view full details of film(s)");
		System.out.println("3) exit");
		System.out.print("Choice: ");

	}
}

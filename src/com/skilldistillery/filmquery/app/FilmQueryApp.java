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
//    app.test();
		app.launch();
	}

//  private void test() {
//    Film film = db.getFilmById(1);
//    System.out.println(film);
//  }

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		int choice = 0;
		DatabaseAccessorObject database = new DatabaseAccessorObject();

		boolean again = true;
		do {
			choice = menuSelect(choice, input);

			switch (choice) {
			case 1:
				again = option1(choice, input, database);
				break;
			case 2:
				again = option2(input, database);
				break;
			case 3:
				System.out.println("Goodbye");
				System.exit(0);
			}

		} while (again);
		System.out.println("Goodbye");

	}

	private void menu() {
		System.out.println("1) Look up a film by its id (1-1000)");
		System.out.println("2) Look up a film by a search keyword");
		System.out.println("3) Exit the application");
		System.out.print("choice: ");
	}

	private int menuSelect(int choice, Scanner input) {
		do {
			try {
				System.out.println("What would you like to do?");
				menu();
				choice = input.nextInt();

				if (choice < 1 || choice > 3) {
					System.err.println(" if Please enter a valid number 1 - 3 ");
					System.out.println();
					choice = 0;
				}
			} catch (Exception e) {
				input.nextLine();
				System.out.println();
				System.err.println(" catch Please enter a valid number 1 - 3: ");
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
				sb.append(", Desctription: " + film.getDescription());
				sb.append(", Language: " + database.getLanguage(film.getLanguage_id()));
				System.out.println(sb);
				System.out.println(film.getCast());
			} else {
				System.err.println("Film not in directory. Try again? (Y/N");
				again = input.next();

			}
		} while (again.equalsIgnoreCase("Y") ? true : false);

		System.out.print("Return to main menu? (Y/N): ");
		again = input.next();

		return again.equalsIgnoreCase("Y") ? true : false;
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
		int i = 1;
		for (Film film : filmList) {
			StringBuilder sb = new StringBuilder();
			sb.append("Title: " + film.getTitle());
			sb.append(", Year: " + film.getYear());
			sb.append(", Rating: " + film.getRating());
			sb.append(", Desctription: " + film.getDescription());
			sb.append(", Language: " + database.getLanguage(film.getLanguage_id()));
			System.out.println(sb);
			System.out.println(film.getCast());

			i++;
		}
		System.out.println("number of films: " + i);

		System.out.print("Return to main menu? (Y/N): ");
		again = input.next();

		return again.equalsIgnoreCase("Y") ? true : false;
	}
}

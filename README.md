## Film Query App
_skill Distillery Week 6 Homework project_

Main app is in "src/com/skilldistillery/filmquery" under APP

### Overview
User sees a menu that asks them to see a film by film id, by keyword, view all films, and exit. Film id takes in an integer and matches the film id with the film in the database. This returns a film object and displays relevant information: Title, Year, Rating, Language, Category, Description, Actors, and Inventory Levels. Keyword search takes in a String that the user puts in and checks the database title and description for the keyword and returns the list of films that match displaying in a similar manner as the first option. View all films will show a list of all the films but only the film ID and title. In each menu option there is a sub menu that asks the user if they want to see full details of the movies or return to the main menu. The full details shows the raw details from the database.

### Tech Used:
* MYSQL
* Java Pillars for Object Oriented Programming(Encapsulation, Polymorphism, Abstraction, Inheritance )
* Database Extraction with Prepared Statement
* Interfaces
* Used: Ternary's, for loops, advanced for loops, while loops, nested if statements,
switch statements, String Builder, Exception handling with Try/Catch statements,
Array Lists, Hash Sets, and Scanner
* RegEx with Pattern Matcher

### Methods I am Proud of:

#### printInventory():
In: DatabaseAccessorObject;
This is were I used RegEx to parse the data using the condition of the films. I broke this into 4 different methods for used, new, lost, and damaged. I had to make a "raw" inventory method that took a film id and gave me a list of movies with the  film id, condition, store id, city, and state. Since the raw data was pulling from the database it would show data in a non-user friendly way. For example: if a store had 3 used movies (from the film id) it would show:

Film Id: 1 Condition: used : at store Id: 1 City : Denver State: Colorado.

Film Id: 1 Condition: used : at store Id: 1 City : Denver State: Colorado.

Film Id: 1 Condition: used : at store Id: 1 City : Denver State: Colorado.

This shows the data as many times as how many films each store had and their condition. So I took this raw data and parsed the information using each of the four methods (used, new, lost, damaged) I then iterated through the list and used a counter variable to document the total number of films for each condition. Then I would put that list into a Hash Set to get rid of the duplicates. I put the set into a list again so I could then iterate through the list and run another RegEx to count how many films each store individually had and reassign them to the list. at the end of the method I put together a String for the total number of films and then concatenated the list.toString to return the final String of inventory.

In the Print method I first checked if each list was null, if not it would print that String. Each line would print used, new, lost, damaged.

#### Option2():
In: FilmQueryApp

I take the users keyword or multiple words. If the String returned was multiple words I split the string on the spaces and added the MYSQL pattern matching symbols to the beginning and end of each word. The new String is then sent to the findFilmByKeyword() in DatabaseAccessorObject where the magic happens. This uses a MYSQL command to find a film by a word(s) either in the title or in the description. The set of keywords doesn't have to occur right next to each other, the words can be in any order but as long as the keywords are present it will print the film details as described above.

### Needs More work

JUnits tests could use more of tests. This is a topic I am searching for more understanding on how to write effective tests. I have some of the easier tests in the program. The tests that I am unsure on how to write are the inventory testing and the keyword tests.

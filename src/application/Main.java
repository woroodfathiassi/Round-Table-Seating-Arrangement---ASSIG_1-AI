package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private Map<String, Person> map = new HashMap();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		readFile();
		System.out.println("Greedy => \n");
		System.out.println(greedyAlgorithm("Ahmed").toString());
		System.out.println("___________________________");
		System.out.println("UCS => \n");
		System.out.println(ucsAlgorithm2("Ahmed").toString());
		System.out.println("___________________________");
		System.out.println("A* => \n");
		System.out.println(aStarAlgorithm2("Ahmed").toString());

	}

	private void readFile() {
		String csvFile = "dislike.csv";
		// read the dislike.csv file
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String[] names = br.readLine().split(","); // split the line by ','
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				Person person = new Person(tokens[0], 0);
				for (int i = 1; i < tokens.length; i++) {
					// save the dislike values to each person on his list
					person.getListOfAdj().add(new Person(names[i],
							(Float.parseFloat(tokens[i].substring(0, tokens[i].length() - 1))) / 100));
				}
				// store the people and the dislike values in the mapوThe key represents the
				// person's name and the value represents his dislike values list
				map.put(tokens[0], person);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Greedy Search
	// used a Priority Queue to checking the lowest dislike value
	// F(n) = H(n) ,dislike value
	// Time Complexity = O(n + log n)
	public ArrayList<String> greedyAlgorithm(String name) {
		PriorityQueue<Person> frontier = new PriorityQueue<>(Comparator.comparingDouble(Person::getLike));
		ArrayList<String> explored = new ArrayList<>();
		Map<String, Person> tempMap = new HashMap<>(map); // Create a copy of the map O(n)
		Person firstOne = tempMap.remove(name);
		double total = 0;
		if (firstOne != null) {
			explored.add(name);

			for (Person neighbor : firstOne.getListOfAdj()) { // O(log k)
//				neighbor.setLike(neighbor.getLike());
				frontier.add(neighbor);
			}

			while (!frontier.isEmpty()) { // O(log n) 

				Person currentPerson = frontier.poll();
				String currentName = currentPerson.getName();

				// Find the lowest cost per person so that they are not already seated
				if (!explored.contains(currentName)) {
					total += currentPerson.getLike();
					// Heap cleaning because greedy does not depend on the previous values of each
					// node
					frontier.clear(); // O(n)	`
					explored.add(currentName);

					// get the dislike values for the current one and put them to the heap
					// O(log n)
					for (Person neighbor : tempMap.get(currentName).getListOfAdj()) { 
						if (!explored.contains(neighbor.getName())) {
							neighbor.setLike(neighbor.getLike());
							frontier.add(neighbor);
						}
					}
				}
			}
			// find the cost between the first person and the last one
			// O(log k)
			for (Person adj : firstOne.getListOfAdj()) {
				if (adj.getName().equals(explored.get(map.size() - 1)))
					total += adj.getLike();
			}
			System.out.println("TOTAL COST => " + total);
		} else {
			System.err.println("Person not found in map.");
		}
		return explored;
	}

	// UCS
	// used a Priority Queue to checking the lowest dislike value
	// F(n) = G(n) ,dislike value ^ 2
	public ArrayList<String> ucsAlgorithm2(String name) {
		PriorityQueue<Seating> frontier = new PriorityQueue<>(Comparator.comparingDouble(Seating::getFinalCost));
		Map<String, Person> tempMap = new HashMap<>(map); // store all values except the fist person
		Person firstOne = tempMap.remove(name);
		ArrayList<String> explored = new ArrayList<>();
		double total = 0;

		if (firstOne != null) {

			// store the first person dislike values in the heap
			ArrayList<String> seating = new ArrayList<>();
			seating.add(firstOne.getName());
			for (Person neighbor : firstOne.getListOfAdj()) {
				ArrayList<String> firstSeatingList = new ArrayList<>();
				firstSeatingList.addAll(seating);
				if (!firstSeatingList.contains(neighbor.getName())) {
					firstSeatingList.add(neighbor.getName());
					// the reach cost is dislike value ^ 2
					Seating temp = new Seating(firstSeatingList, neighbor.getLike() * neighbor.getLike());
					frontier.add(temp);
				}
			}

			while (!frontier.isEmpty()) {
				Seating currentSeating = frontier.poll();

				// the total cost = the cost from first person to the last one
				total = currentSeating.getFinalCost();

				explored = currentSeating.getSeatingArrangement();
				String currentPerson = explored.get(explored.size() - 1);

				// Algorithm finishes when all people are seated
				if (explored.size() == map.size()) {
					break;
				}
				for (Person neighbor : tempMap.get(currentPerson).getListOfAdj()) {

					ArrayList<String> neighborList = new ArrayList<>();
					neighborList.addAll(explored);
					if (!neighborList.contains(neighbor.getName())) {
						// Heap contains all the possibilities of where people can sit with their own
						// cost value
						neighborList.add(neighbor.getName());
						Seating newSeating = new Seating(neighborList, total + neighbor.getLike() * neighbor.getLike());
						frontier.add(newSeating);
					}
				}
			}
			// find the cost between the first person and the last one
			for (Person adj : firstOne.getListOfAdj()) {
				if (adj.getName().equals(explored.get(map.size() - 1)))
					total += (adj.getLike() * adj.getLike());
			}
			System.out.println("TOTAL COST => " + total);
		} else {
			System.err.println("Person not found in map.");
		}
		return explored;
	}

	// A* Search
	// used a Priority Queue to checking the lowest dislike value
	// F(n) = H(n) + G(n) , dislike value + (dislike value ^ 2)
	public ArrayList<String> aStarAlgorithm2(String name) {
		PriorityQueue<Seating> frontier = new PriorityQueue<>(Comparator.comparingDouble(Seating::getFinalCost));
		Map<String, Person> tempMap = new HashMap<>(map);
		Person firstOne = tempMap.remove(name);
		ArrayList<String> explored = new ArrayList<>(); // null;

		double total = 0;

		if (firstOne != null) {

			ArrayList<String> seating = new ArrayList<>();
			seating.add(firstOne.getName());

			// store the first person dislike values in the heap
			for (Person neighbor : firstOne.getListOfAdj()) {
				ArrayList<String> firstSeatingList = new ArrayList<>();
				firstSeatingList.addAll(seating);
				if (!firstSeatingList.contains(neighbor.getName())) {
					firstSeatingList.add(neighbor.getName());
					// the reach cost is dislike value ^ 2
					double reachCost = neighbor.getLike() * neighbor.getLike();
					// the final cost = reach cost + dislike value( h(n) )
					Seating temp = new Seating(firstSeatingList, reachCost + neighbor.getLike());
					temp.setReachCost(reachCost);
					frontier.add(temp);
				}
			}
			while (!frontier.isEmpty()) {
				Seating currentSeating = frontier.poll();
				double reachCost = currentSeating.getReachCost();
				total = currentSeating.getFinalCost();

				explored = currentSeating.getSeatingArrangement();
				String currentPerson = explored.get(explored.size() - 1);

				// Algorithm finishes when all people are seated
				if (explored.size() == map.size()) {
					break;
				}
				for (Person neighbor : tempMap.get(currentPerson).getListOfAdj()) {

					ArrayList<String> neighborList = new ArrayList<>();
					neighborList.addAll(explored);
					if (!neighborList.contains(neighbor.getName())) {
						// Heap contains all the possibilities of where people can sit with their own
						// cost value
						neighborList.add(neighbor.getName());
						Seating newSeating = new Seating(neighborList, reachCost + neighbor.getLike());
						newSeating.setReachCost(reachCost + (neighbor.getLike()));
						frontier.add(newSeating);
					}
				}
			}
			// find the cost between the first person and the last one
			for (Person adj : firstOne.getListOfAdj()) {
				if (adj.getName().equals(explored.get(map.size() - 1)))
					total += (adj.getLike());
			}
			System.out.println("TOTAL COST => " + total);
		} else {
			System.err.println("Person not found in map.");
		}
		return explored;
	}
}
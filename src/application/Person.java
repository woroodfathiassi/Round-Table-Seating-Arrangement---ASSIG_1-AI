package application;

import java.util.ArrayList;

public class Person {
	double cost;
	private String name;
	private double like;
	private ArrayList<Person> listOfAdj = new ArrayList<>();

	public Person() {

	}

	public Person(String name, double like) {
		super();
		this.name = name;
		this.like = like;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLike() {
		return like;
	}

	public void setLike(double like) {
		this.like = like;
	}

	public ArrayList<Person> getListOfAdj() {
		return listOfAdj;
	}

	public void setListOfAdj(ArrayList<Person> listOfAdj) {
		this.listOfAdj = listOfAdj;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return name + " " + like;
	}

	public String print() {
		return name + " " + like + " " + listOfAdj;
	}

}

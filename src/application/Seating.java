package application;

import java.util.ArrayList;

public class Seating {
	private ArrayList<String> seatingArrangement;
	private double finalCost;
	private double reachCost;

	public Seating(ArrayList<String> seatingArrangement, double finalCost) {
		super();
		this.seatingArrangement = seatingArrangement;
		this.finalCost = finalCost;
	}

	public ArrayList<String> getSeatingArrangement() {
		return seatingArrangement;
	}

	public void setSeatingArrangement(ArrayList<String> seatingArrangement) {
		this.seatingArrangement = seatingArrangement;
	}

	public double getFinalCost() {
		return finalCost;
	}

	public void setFinalCost(double finalCost) {
		this.finalCost = finalCost;
	}

	public double getReachCost() {
		return reachCost;
	}

	public void setReachCost(double reachCost) {
		this.reachCost = reachCost;
	}

	@Override
	public String toString() {
		return "Seating [seatingArrangement=" + seatingArrangement + ", finalCost=" + finalCost + ", reachCost="
				+ reachCost + "]";
	}

}

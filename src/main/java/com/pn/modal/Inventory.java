package com.pn.modal;

public class Inventory {
	
	private static Inventory myObj;
	
	private Inventory() {	
	}
	
	public static Inventory getInstance() {
	    if(myObj == null){
	        myObj = new Inventory();
	    }
	    return myObj;
	}
	
	private int sugar;
	private int milk;
	private int coffee;
	
	public int getSugar() {
		return sugar;
	}
	public void setSugar(int sugar) {
		this.sugar = sugar;
	}
	public int getMilk() {
		return milk;
	}
	public void setMilk(int milk) {
		this.milk = milk;
	}
	public int getCoffee() {
		return coffee;
	}
	public void setCoffee(int coffee) {
		this.coffee = coffee;
	}	
}

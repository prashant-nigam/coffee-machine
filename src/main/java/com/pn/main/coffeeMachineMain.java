package com.pn.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pn.modal.Drink;
import com.pn.modal.Inventory;
import com.pn.modal.Recipe;

public class coffeeMachineMain {
	List<Drink> drinkList = new ArrayList<>();
	public static void main(String[] args) {
		try {
			coffeeMachineMain machine = new coffeeMachineMain();
			machine.loadData();
			machine.startIO();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void startIO() {
    	showMenu();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        //running loop
        while(true){            
            try {                
                input = reader.readLine().toLowerCase().trim();
                if(input.equals("")){
                    continue;
                }else if (input.equals("q")){ 
                    System.exit(0); 
                }else if(input.equals("r")){
                }else if(input.equals("i")){
                	showInventory();
                }else if(Integer.parseInt(input) > 0 && Integer.parseInt(input) <= drinkList.size()){ //dynamic drink menu selection
                	makeDrink(Integer.parseInt(input));                	
                }else{
                    throw new IOException();//legal, but invalid input
                }
                System.out.println("\n\n ---------------------------------------------------------------------------------- \n\n");
            	showMenu();
            } catch (Exception e) {
                System.out.println("Invalid selection: " + input + "\n");//illegal input
            }
        }//running loop     
    }
    
    private void loadData() throws Exception {
    	try {
			String fileName = "data.json";
			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());			 
			System.out.println("File Found : " + file.exists());			 
			
			if(file.exists()) {
				//Read File Content
				String content = new String(Files.readAllBytes(file.toPath()));
				System.out.println(content);
				
				JSONObject data =  new JSONObject(content);  
				
				JSONObject jsoninventory =  (JSONObject) data.get("inventory");
				
				// Loading Inventory
				Inventory stock = Inventory.getInstance();				
				stock.setSugar(jsoninventory.getInt("sugar"));
				stock.setCoffee(jsoninventory.getInt("coffee"));
				stock.setMilk(jsoninventory.getInt("milk"));
				
				//Loading Drinks
				JSONArray jsonDrinks = data.getJSONArray("drinks");
				
				for (int i = 0; i < jsonDrinks.length(); i++) {
					JSONObject obj = (JSONObject) jsonDrinks.get(i);
					Drink drink = new Drink();
					drink.setName(obj.getString("name"));
					JSONObject jsonRecipe = (JSONObject) obj.get("recipe");
					Recipe recipe = new Recipe();
					recipe.setSugar(jsonRecipe.getInt("sugar"));
					recipe.setCoffee(jsonRecipe.getInt("coffee"));
					recipe.setMilk(jsonRecipe.getInt("milk"));
					drink.setRecipe(recipe);
					drinkList.add(drink);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException ex) {
			ex.printStackTrace();
			throw ex;
		}
    }
    
    private boolean makeDrink(int drinkIdx) { 
    	Inventory inventory = Inventory.getInstance();
    	Drink drink = drinkList.get(drinkIdx-1);
    	
    	// Check Inventory
    	if(inventory.getCoffee() - drink.getRecipe().getCoffee() < 0 || inventory.getMilk() - drink.getRecipe().getMilk() < 0
    			|| inventory.getSugar() - drink.getRecipe().getSugar() < 0) {
    		System.out.println("Not enough inventory for preparing drink..!!");
    		return false;
    	}
    	
    	System.out.println("Preparing "+drink.getName()+"....");
    	
    	// updating inventory
    	inventory.setCoffee(inventory.getCoffee() - drink.getRecipe().getCoffee());
    	inventory.setMilk(inventory.getMilk() - drink.getRecipe().getMilk());
    	inventory.setSugar(inventory.getSugar() - drink.getRecipe().getSugar());
    	
    	System.out.println(drink.getName()+" prepared....");
    	
    	return true;
    }
    
    private void showMenu() {
    	int i = 1;
    	System.out.println("#### Menu ####");
    	 for (Drink drink : drinkList) { 
			System.out.println("  "+i+". "+ drink.getName());
			i++;
		}
    	 System.out.println("Please choose your drink :"); 
    }
    
    private void showInventory() {
    	System.out.println("######## Inventory ########");
    	System.out.println(" Sugar : "+ Inventory.getInstance().getSugar()+"gm");
    	System.out.println(" Coffee : "+ Inventory.getInstance().getCoffee()+"gm");
    	System.out.println(" Milk : "+ Inventory.getInstance().getMilk()+"ml");
    }
}

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class Inventory implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4114041844129677745L;
	private static ArrayList<Item> items;
    private static ArrayList<Integer> quantity;
    private static ArrayList<String> category;
    private static ArrayList<Double> purchaseCost;
    private static ArrayList<Date> purchaseDate;
    
    static {
        items = new ArrayList<>();
        quantity = new ArrayList<>();
        category = new ArrayList<>();
        purchaseCost = new ArrayList<>();
        purchaseDate = new ArrayList<>();
    }

    private Inventory() {
    }
    
    public static ArrayList<Item> getItems(){
    	return items;
    }
    
    public static ArrayList<Integer> getQuantity() {
        return quantity;
    }

    public static ArrayList<String> getCategory() {
        return category;
    }
    
    public static ArrayList<Double> getPurchaseCost() {
        return purchaseCost;
    }
    
    public static ArrayList<Date> getPurchaseDate() {
        return purchaseDate;
    }

    public static void addCategory(String catg) {
    	if (catg == null || catg.trim().isEmpty()) {
                  throw new IllegalArgumentException("Category cannot be null or empty");
        }
    	if(!category.contains(catg)) {
    		category.add(catg);
    	}
    }
        
    
    public static double getTotalPurchaseCost(String Option) {
        if (Option == null || Option.trim().isEmpty()) {
            throw new IllegalArgumentException("Option cannot be null or empty");
        }
        
        double totalCost = 0.0;
        Date currentDate = new Date();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);

        for (int i = 0; i < purchaseDate.size(); i++) {
            Date itemDate = purchaseDate.get(i);
            Calendar itemCalendar = Calendar.getInstance();
            itemCalendar.setTime(itemDate);

            if (Option.equalsIgnoreCase("daily")) {
                if (currentCalendar.get(Calendar.YEAR) == itemCalendar.get(Calendar.YEAR) &&
                    currentCalendar.get(Calendar.DAY_OF_YEAR) == itemCalendar.get(Calendar.DAY_OF_YEAR)) {
                    totalCost += purchaseCost.get(i);
                }
            } else if (Option.equalsIgnoreCase("monthly")) {
                if (currentCalendar.get(Calendar.YEAR) == itemCalendar.get(Calendar.YEAR) &&
                    currentCalendar.get(Calendar.MONTH) == itemCalendar.get(Calendar.MONTH)) {
                    totalCost += purchaseCost.get(i);
                }
            } else if (Option.equalsIgnoreCase("annually")) {
                if (currentCalendar.get(Calendar.YEAR) == itemCalendar.get(Calendar.YEAR)) {
                    totalCost += purchaseCost.get(i);
                }
            } else {
                throw new IllegalArgumentException("Invalid option provided. Valid options are: daily, monthly, annually.");
            }
        }
        return totalCost;
    }

       
    public static String notifyManagerOnLowItemStock() {
        StringBuilder notification= new StringBuilder();
    	for (int i = 0; i < quantity.size(); i++) {
            if (quantity.get(i) < 5) {
            	notification.append("Low stock for item: ").append(items.get(i).getName()).append("\n");
            }
        }
    	return notification.toString();
    }

    
    public static String notifyManagerOnLowCategoryStock() {

        ArrayList<Integer> categoryQuantity = new ArrayList<>();
        
        for (int i = 0; i < category.size(); i++) {
            categoryQuantity.add(0);
        }
        
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String itemCategory = item.getCategory();
            int itemQuantity = quantity.get(i);

            for (int j = 0; j < category.size(); j++) {
                if (category.get(j).equals(itemCategory)) {
                    categoryQuantity.set(j, categoryQuantity.get(j) + itemQuantity);
                    break;
                }
            }
        }

        StringBuilder notification = new StringBuilder();
        for (int i = 0; i < category.size(); i++) {
            if (categoryQuantity.get(i) < 5) {
                notification.append("Low stock for category: ").append(category.get(i)).append("\n");
            }
        }
        return notification.toString();
    }
    
    public static void addItemtoInventory(String name, String category, Supplier supplier, Date purchaseDate, double purchasePrice, double sellingPrice, int quantity) {
    	getItems().add(new Item(name, category, supplier, purchaseDate, purchasePrice, sellingPrice));
    	Inventory.getQuantity().add(quantity);
    	if(!Inventory.category.contains(category)) {
    		Inventory.category.add(category);
    	}
    	Inventory.addCategory(category);
    	Inventory.purchaseDate.add(purchaseDate);
    	Inventory.getPurchaseCost().add(purchasePrice*quantity);
    }
}




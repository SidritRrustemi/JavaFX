package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class Manager extends Employee implements Serializable{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -5855963302874211733L;
	private ArrayList<Sector> managerSector;
    private ArrayList<Supplier> suppliers;
    private String notification;
    private boolean permShowCashierPerformance;
    private boolean permAddItemInventory;
    private boolean permCreateCategory;
    private boolean permShowStatisticsAboutItems;

    public Manager(String empname, String empsurname, Date dob, String phonenr, String email, double salary, String addr,ArrayList<Sector> sector) {
        super(empname, empsurname, dob, phonenr, email, salary, addr);
        
        if (sector == null || sector.isEmpty()) {
            throw new IllegalArgumentException("Manager must be assigned at least one sector.");
        }
        this.managerSector = sector;     
        this.suppliers = new ArrayList<>();
        this.permShowCashierPerformance = true;
        this.permAddItemInventory = true;
        this.permCreateCategory = true;
        this.permShowStatisticsAboutItems = true;
        this.role = "Manager";
    }

    //overloaded methods

    public int showNumberOfBills(String EmployeeID, Date date) {
    	
        if (!permShowCashierPerformance) {
            throw new SecurityException("Permission denied to show cashier performance.");
        }

        validateDate(date);
        Cashier cashier = findCashierById(EmployeeID);
        
        if (cashier == null) {
            throw new IllegalArgumentException("No cashier found with the given EmployeeID: " + EmployeeID);
        }

        int count = 0;
        for (Bill bill : cashier.getBills()) {
            if (isSameDay(bill.getSaleDate(), date)) {
                count++;
            }
        }
        return count;
    }

    public int showNumberOfBills(String EmployeeID, Date date1, Date date2) {

        if (!permShowCashierPerformance) {
            throw new SecurityException("Permission denied to show cashier performance.");
        }
        
        validateDateRange(date1, date2);
        Cashier cashier = findCashierById(EmployeeID);

        if (cashier == null) {
            throw new IllegalArgumentException("No cashier found with the given EmployeeID: " + EmployeeID);
        }

        int count = 0;
        for (Bill bill : cashier.getBills()) {
            if (isBetweenDates(bill.getSaleDate(), date1, date2)) {
                count++;
            }
        }
        return count;
    }

    public int showNumberOfItemsSold(String EmployeeID, Date date) {
         if (!permShowCashierPerformance) {
             throw new SecurityException("Permission denied to show cashier performance.");
         }

         validateDate(date);
         Cashier cashier = findCashierById(EmployeeID);

         if (cashier == null) {
             throw new IllegalArgumentException("No cashier found with the given EmployeeID: " + EmployeeID);
         }

         int totalItems = 0;
         for (Bill bill : cashier.getBills()) {
             if (isSameDay(bill.getSaleDate(), date)) {
                 totalItems += bill.getTotalQuantity();
             }
         }
         return totalItems;
    }

    public int showNumberOfItemsSold(String EmployeeID, Date date1, Date date2) {

        if (!permShowCashierPerformance) {
            throw new SecurityException("Permission denied to show cashier performance.");
        }

        validateDateRange(date1, date2);
        Cashier cashier = findCashierById(EmployeeID);

        if (cashier == null) {
            throw new IllegalArgumentException("No cashier found with the given EmployeeID: " + EmployeeID);
        }

        int totalItems = 0;
        for (Bill bill : cashier.getBills()) {
            if (isBetweenDates(bill.getSaleDate(), date1, date2)) {
                totalItems += bill.getTotalQuantity();
            }
        }
        return totalItems;
    }

    public double showTotalRevenue(String EmployeeID, Date date) {

        if (!permShowCashierPerformance) {
            throw new SecurityException("Permission denied to show cashier performance.");
        }

        validateDate(date);
        Cashier cashier = findCashierById(EmployeeID);

        if (cashier == null) {
            throw new IllegalArgumentException("No cashier found with the given EmployeeID: " + EmployeeID);
        }
        
        double totalRevenue = 0.0;
        for (Bill bill : cashier.getBills()) {
            if (isSameDay(bill.getSaleDate(), date)) {
                totalRevenue += bill.getTotalAmount();
            }
        }
        return totalRevenue;
    }

    public double showTotalRevenue(String EmployeeID, Date date1, Date date2) {

        if (!permShowCashierPerformance) {
            throw new SecurityException("Permission denied to show cashier performance.");
        }

        validateDateRange(date1, date2);
        Cashier cashier = findCashierById(EmployeeID);

        if (cashier == null) {
            throw new IllegalArgumentException("No cashier found with the given EmployeeID: " + EmployeeID);
        }
        
        double totalRevenue = 0.0;
        for (Bill bill : cashier.getBills()) {
            if (isBetweenDates(bill.getSaleDate(), date1, date2)) {
                totalRevenue += bill.getTotalAmount();
            }
        }
        return totalRevenue;
    }

    private Cashier findCashierById(String employeeID) {
        for (Employee employee : Administrator.getEmployees()) {
            if (employee instanceof Cashier && employee.getEmployeeID().equals(employeeID)) {
                return (Cashier) employee;
            }
        }
        return null;
    }

    private void validateDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
    }

    private void validateDateRange(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Date range cannot have null values.");
        }
        if (date1.after(date2)) {
            throw new IllegalArgumentException("Start date cannot be after the end date.");
        }

    }

    public boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        cal1.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);

        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }   

    public boolean isBetweenDates(Date targetDate, Date startDate, Date endDate) {
        Calendar calTarget = Calendar.getInstance();
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();

        calTarget.setTime(targetDate);
        calStart.setTime(startDate);
        calEnd.setTime(endDate);

        calTarget.set(Calendar.HOUR_OF_DAY, 0);
        calTarget.set(Calendar.MINUTE, 0);
        calTarget.set(Calendar.SECOND, 0);
        calTarget.set(Calendar.MILLISECOND, 0);

        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);

        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);

        return !calTarget.before(calStart) && !calTarget.after(calEnd);
    }
       
    //show statistics
    public double showStatisticsAboutItemsSold(String option) {
        if (!permShowStatisticsAboutItems) {
            throw new SecurityException("Permission denied to show statistics about items.");
        }
        validateOption(option);

        ArrayList<Employee> employees = Administrator.getEmployees();
        double totalRevenue = 0;

        for (Employee employee : employees) {
            if (employee instanceof Cashier) {
                Cashier cashier = (Cashier) employee;
                for (Bill bill : cashier.getBills()) {
                    if (isDateMatchingOption(bill.getSaleDate(), option)) {
                        ArrayList<Item> itemsSold = bill.getItemsSold();
                        ArrayList<Integer> quantitiesSold = bill.getQuantitiesSold();

                        System.out.printf("%-15s%-10s%n", "Item", "Quantity");
                        for (int i = 0; i < itemsSold.size(); i++) {
                            System.out.println(itemsSold.get(i).getName()+"  "+quantitiesSold.get(i));
                        }
                        System.out.println("Bill Total: " + bill.getTotalAmount());
                        totalRevenue += bill.getTotalAmount();
                    }
                }
            }
        }

        System.out.println("Total Revenue for Matching Bill Sale Dates (" + option + ") is " + totalRevenue);
        return totalRevenue;
    }

    public double showStatisticsAboutItemsPurchased(String option) {
        if (!permShowStatisticsAboutItems) {
            throw new SecurityException("Permission denied to show statistics about items.");
        }
        validateOption(option);

        ArrayList<Date> purchaseDates = Inventory.getPurchaseDate();
        ArrayList<Double> purchaseCosts = Inventory.getPurchaseCost();
        double totalPurchaseCost = 0;

        for (int i = 0; i < purchaseDates.size(); i++) {
            if (isDateMatchingOption(purchaseDates.get(i), option)) {
                totalPurchaseCost += purchaseCosts.get(i);
            }
        }
        System.out.println("Total Purchase Cost for Matching Dates (" + option + ") is " + totalPurchaseCost);
        return totalPurchaseCost;
    }

    private boolean isDateMatchingOption(Date date, String option) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar today = Calendar.getInstance();

        switch (option.toLowerCase()) {
            case "daily":
                return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                		calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
            case "monthly":
                return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH);
            case "annually":
                return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR);
            default:
                return false;
        }
    }
    
    private void validateOption(String option) {
        if (option == null || option.trim().isEmpty()) {
            throw new IllegalArgumentException("Option cannot be null or empty.");
        }
        if (!option.equalsIgnoreCase("daily") &&
            !option.equalsIgnoreCase("monthly") &&
            !option.equalsIgnoreCase("annually")) {
            throw new IllegalArgumentException("Invalid option. Valid options are: 'daily', 'monthly', or 'annually'.");
        }
    } 
    
    public void addItemInventory(Item item, int quantity) {
        if (!permAddItemInventory) {
            throw new SecurityException("Permission denied to add items to inventory.");
        }
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        boolean itemExists = false;
        for (int i = 0; i < Inventory.getItems().size(); i++) {
            if (Inventory.getItems().get(i).getName().equals(item.getName()) &&
                Inventory.getItems().get(i).getPurchasePrice() == item.getPurchasePrice() &&
                Inventory.getItems().get(i).getPurchaseDate().equals(item.getPurchaseDate())) {

                itemExists = true;
                Inventory.getQuantity().set(i, Inventory.getQuantity().get(i) + quantity);
                Inventory.getPurchaseCost().set(i, Inventory.getPurchaseCost().get(i) + item.getPurchasePrice() * quantity);
                break;
            }
        }

        if (!itemExists) {
            Inventory.getItems().add(item);
            Inventory.getQuantity().add(quantity);
            Inventory.getPurchaseCost().add(item.getPurchasePrice() * quantity);
            Inventory.getPurchaseDate().add(item.getPurchaseDate());

            if (!Inventory.getCategory().contains(item.getCategory())) {
                Inventory.getCategory().add(item.getCategory());
            }
        }
    }
  
    public void createItemCategory(String category) {
        if (!permCreateCategory) {
            throw new SecurityException("Permission denied to create a new category.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        if (!Inventory.getCategory().contains(category)) {
            Inventory.addCategory(category);
        } else {
            System.out.println("Category " + category + " already exists.");
        }
    }

    public void setNotification(String notification) {
    	if (notification == null || notification.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification cannot be null or empty.");
        }
    	this.notification = notification;
    }

    public void setPermShowCashierPerformance(boolean value) {
    	this.permShowCashierPerformance = value;
    }

    public void setPermAddItemInventory(boolean value) {
    	this.permAddItemInventory = value;
    }

    public void setPermCreateCategory(boolean value) {
    	this.permCreateCategory = value;
    }

    public void setPermShowStatisticsAboutItems(boolean value) {
    	this.permShowStatisticsAboutItems = value;
    }
    
    public boolean getPermShowCashierPerformance() {
    	return permShowCashierPerformance;
    }

    public boolean getPermAddItemInventory() {
    	return permAddItemInventory;
    }

    public boolean getPermCreateCategory() {
    	return permCreateCategory;
    }

    public boolean getPermShowStatisticsAboutItems() {
    	return permShowStatisticsAboutItems;
    }

    public void addManagerSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("Sector cannot be null.");
        }
        if (!managerSector.contains(sector)) {
            managerSector.add(sector);
        } else {
            System.out.println("Sector already assigned to the manager.");
        }
    }

    public void removeManagerSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("Sector cannot be null.");
        }
        if (managerSector.contains(sector)) {
            managerSector.remove(sector);
        } else {
            System.out.println("Sector not found in the manager's list.");
        }
    }

    public ArrayList<Supplier> getSupplierList() { return suppliers; }	
    
    public ArrayList<Sector> getSectorList() { return managerSector; }	
    
    public void setSectorList(ArrayList<Sector> managerSector) { this.managerSector = managerSector; }

}

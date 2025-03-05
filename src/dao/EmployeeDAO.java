package dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import model.Administrator;
import model.Cashier;
import model.Employee;
import model.Inventory;
import model.Item;
import model.Manager;
import model.Sector;
import model.Supplier;

public class EmployeeDAO{
	
	public static final String EMPLOYEES_FILE_PATH = "src/dao/employees.dat";
	private static final File EMPLOYEES_FILE = new File(EMPLOYEES_FILE_PATH);
	
	public static final String INVENTORY_FILE_PATH = "src/dao/inventory.dat";
	private static final File INVENTORY_FILE = new File(INVENTORY_FILE_PATH);

	private static ArrayList<Employee> employees = new ArrayList<>();

	public void getAvailableData() {
		getAvailableEmployees();
		getAvailableInventory();
	}
	
	public ArrayList<Employee> getAllEmployees() {
		if (employees.isEmpty()) {
			getAvailableEmployees();
		}
		return employees;
	}
	
	public void getAvailableEmployees(){
		if(!EMPLOYEES_FILE.exists() || EMPLOYEES_FILE.length()== 0) {
			seedEmployeeData();
			System.out.println("File size after seeding " + EMPLOYEES_FILE);
		} else {
			readFromEmployeeFile();
		}	
	}
	
	public void readFromEmployeeFile() {
		Administrator.getEmployees().clear();
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(EMPLOYEES_FILE))) {
			while (true) {
				Employee emp = (Employee)inputStream.readObject();
				employees.add(emp);
				if(!(emp instanceof Administrator)) {
					Administrator.getEmployees().add(emp);
				}
			}
		} catch (EOFException ex) {
			System.out.println("Data read from binary file.");
		} catch (IOException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void seedEmployeeData() {
	
		Employee admin = new Administrator("Sidrit", "Rrustemi", new Date(1000000), "0671234567", "srrustemi23@epoka.edu.al", 2500.0, "komuna");
		
		ArrayList<Sector> sectormanager1 = new ArrayList<>();
		sectormanager1.add(Sector.Gaming);
		sectormanager1.add(Sector.Computers);
		sectormanager1.add(Sector.MobileDevices);
		Employee manager1 = new Manager("Arsildo", "Veliu", new Date(1000000), "0671231231", "arveliu23@epoka.edu.al", 1100.0, "5 maji", sectormanager1);
		
		ArrayList<Sector> sectormanager2 = new ArrayList<>();
		sectormanager2.add(Sector.Accessories);
		sectormanager2.add(Sector.HomeAppliances);
		sectormanager2.add(Sector.SmallElectronics);
		sectormanager2.add(Sector.Camera);
		Employee manager2 = new Manager("Viola", "Zeneli", new Date(1000000), "0687777777", "vzeneli23@epoka.edu.al", 1200.0, "komuna", sectormanager2);
		
		Employee cashier1 = new Cashier(Sector.SmallElectronics, "Megi", "Almadhi", new Date(1000000), "0688898999", "malmadhi23@epoka.edu.al", 500.0, "shkoze");
		Employee cashier2 = new Cashier(Sector.Camera, "Marsi", "Veliu", new Date(1000000), "0696696969", "mveliu24@epoka.edu.al", 500.0, "bllok");
		Employee cashier3 = new Cashier(Sector.Gaming, "Argi", "Veliu", new Date(1000000), "0699969696", "aveliu24@epoka.edu.al", 500.0, "bllok");
		
		employees.add(admin);
		employees.add(manager1);
		employees.add(manager2);
		employees.add(cashier1);
		employees.add(cashier2);
		employees.add(cashier3);
			
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(EMPLOYEES_FILE))) {
			for (Employee emp : employees) {
				outputStream.writeObject(emp);
				if(!(emp instanceof Administrator)) {
					Administrator.getEmployees().add(emp);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void writeEmployeeToFile() {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(EMPLOYEES_FILE, false))) {
			outputStream.writeObject(employees.get(0));
			for (Employee emp : Administrator.getEmployees()) {
				outputStream.writeObject(emp);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void getAvailableInventory(){
		if(!INVENTORY_FILE.exists() || INVENTORY_FILE.length()== 0) {
			seedInventoryData();
			System.out.println("File size after seeding " + INVENTORY_FILE);
		} else {
			readFromInventoryFile();
		}	
	}
	
	public void seedInventoryData() {
		ArrayList<String> supplierItems1 = new ArrayList<>();
		supplierItems1.add("Iphone");
		supplierItems1.add("Laptop");
		supplierItems1.add("Ipad");
		supplierItems1.add("AirTag");
		Supplier supplier1 = new Supplier("Apple", "0683443234", "California", supplierItems1);
		((Manager)(employees.get(1))).getSupplierList().clear();
		Administrator.addSupplier(supplier1, (Manager)employees.get(1));
		
		ArrayList<String> supplierItems2 = new ArrayList<>();
		supplierItems2.add("TV");
		supplierItems2.add("Projector");
		supplierItems2.add("PlayStation");
		Supplier supplier2 = new Supplier("Fredi shpk", "0672121221", "California", supplierItems2);
		((Manager)(employees.get(2))).getSupplierList().clear();
		Administrator.addSupplier(supplier2, (Manager)employees.get(2));
		
		writeEmployeeToFile();

		Inventory.addItemtoInventory("Iphone", "Phone", supplier1, new Date(), 600.0, 1100.0, 30);
		Inventory.addItemtoInventory("TV", "TV", supplier2, new Date(), 400.0, 800.0, 15);
		Inventory.addItemtoInventory("Laptop", "Computer", supplier1, new Date(), 450.0, 750.0, 20);
		Inventory.addItemtoInventory("Iphone", "Phone", supplier1, new Date(), 700.0, 1100.0, 30);
		Inventory.addItemtoInventory("TV", "TV", supplier2, new Date(), 500.0, 800.0, 15);
		Inventory.addItemtoInventory("Laptop", "Computer", supplier1, new Date(), 550.0, 750.0, 20);
		Inventory.addItemtoInventory("Iphone", "Phone", supplier1, new Date(), 800.0, 1100.0, 30);	
	
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
			outputStream.writeObject(Inventory.getItems());
			outputStream.writeObject(Inventory.getQuantity());
			outputStream.writeObject(Inventory.getCategory());
			outputStream.writeObject(Inventory.getPurchaseCost());
			outputStream.writeObject(Inventory.getPurchaseDate());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void readFromInventoryFile() {
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(INVENTORY_FILE))) {
			Inventory.getItems().clear();
	        Inventory.getQuantity().clear();
	        Inventory.getCategory().clear();
	        Inventory.getPurchaseCost().clear();
	        Inventory.getPurchaseDate().clear();
	        
			ArrayList<Item> items = (ArrayList<Item>)inputStream.readObject();
			for(Item item: items) {
				Inventory.getItems().add(item);
			}
			ArrayList<Integer> quantities = (ArrayList<Integer>)inputStream.readObject();
			for(Integer i: quantities) {
				Inventory.getQuantity().add(i);
			}
			ArrayList<String> categories = (ArrayList<String>)inputStream.readObject();
			for(String s: categories) {
				Inventory.getCategory().add(s);
			}
			ArrayList<Double> costs = (ArrayList<Double>)inputStream.readObject();
			for(Double d: costs) {
				Inventory.getPurchaseCost().add(d);
			}
			ArrayList<Date> dates = (ArrayList<Date>)inputStream.readObject();
			for(Date d: dates) {
				Inventory.getPurchaseDate().add(d);
			}
			System.out.println("Data read from binary file.");
		} catch (EOFException ex) {
			
		} catch (IOException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void writeInventoryToFile() {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
			outputStream.writeObject(Inventory.getItems());
			outputStream.writeObject(Inventory.getQuantity());
			outputStream.writeObject(Inventory.getCategory());
			outputStream.writeObject(Inventory.getPurchaseCost());
			outputStream.writeObject(Inventory.getPurchaseDate());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
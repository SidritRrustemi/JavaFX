package controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import dao.EmployeeDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Administrator;
import model.Cashier;
import model.Employee;
import model.Manager;
import model.Sector;
import model.Supplier;
import view.AdministratorView;

public class AdministratorController {

	private final AdministratorView view;
	private final EmployeeDAO employeeDAO;
	
	public AdministratorView getView() {
		return view;
	}
	
	public AdministratorController(Stage stage, Employee employee) {

		this.view = new AdministratorView(stage, employee);
		employeeDAO = new EmployeeDAO();

		setSidebarOnClickActions();
		populateEmployees();
		searchEmployees();
		setAddEmployee();
		updateEmployee();
		setRemoveEmployee();
		setChangePasswordUser();
		populateSuppliers();
		searchSupplier();
		setAddSupplier();
		setUpdateSupplier();
		setRemoveSupplier();
		setSaveProfile();
		setChangePasswordAdmin();
	}
	
	private void setSidebarOnClickActions() {
        this.view.getEmployeesButton().setOnAction(e -> this.view.displayEmployeeView());
        this.view.getViewTotalCostButton().setOnAction(e -> this.view.displayViewTotalCostView());
        this.view.getTotalIncomeButton().setOnAction(e -> this.view.displayViewTotalIncomeView());
        this.view.getSupplierButton().setOnAction(e -> this.view.displaySupplierView());
        this.view.getProfileButton().setOnAction(e -> this.view.displayProfileView());
        this.view.getLogoutButton().setOnAction(e -> this.view.navigateToLogin());
	}
	
	private void populateEmployees() {
		this.view.getEmpFlowPane().getChildren().clear();
		for (Employee employee: this.view.getEmployees()) {
			Button empButton = this.view.createEmployeeCard(employee, view.getEmpButtons(), this.view.getEmpPane(), this.view.getEmployees(), this.view.getIsExpandedEmp());
        	this.view.getEmpFlowPane().getChildren().add(empButton);
        }
	}
	
	private void searchEmployees() {
		this.view.getSearchEmployeeField().setOnKeyTyped(e -> {
            String searchText = this.view.getSearchEmployeeField().getText();
            this.view.getEmpFlowPane().getChildren().clear();
            for (Employee emp: this.view.getEmployees()) {
            	if(emp.getEmployeeName().toLowerCase().contains(searchText.toLowerCase()) || searchText.trim().isEmpty()) {
            		Button empButton = this.view.createEmployeeCard(emp, this.view.getEmpButtons(), this.view.getEmpPane(), this.employeeDAO.getAllEmployees(), this.view.getIsExpandedEmp());
                	this.view.getEmpFlowPane().getChildren().add(empButton);
            	}
            }
            if(this.view.getEmpFlowPane().getChildren().isEmpty()) {
            	Text noResultsText = new Text("No results found!");
                noResultsText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                noResultsText.setFill(Color.DARKGRAY);

                StackPane centeredTextContainer = new StackPane(noResultsText);
                this.view.getEmpFlowPane().getChildren().add(centeredTextContainer);
            }
        });
	}
	
	private void setAddEmployee() {
		view.getSubmitEmpButton().setOnAction(e -> {
            String empNameValue = this.view.getEmpNameField().getText();
            String empSurnameValue = this.view.getEmpSurnameField().getText();
            LocalDate selectedDoB = this.view.getDoBField().getValue();
            Date DoBValue = Date.valueOf(selectedDoB);
            String phoneNrValue = this.view.getPhoneNrField().getText();
            String emailValue = this.view.getEmailField().getText();
            String salaryValue = this.view.getSalaryField().getText();
            String addressValue = this.view.getAddressField().getText();

            String roleValue = "";
            if (this.view.getCashierButton().isSelected()) {
                roleValue = this.view.getCashierButton().getText();
            } else if (this.view.getManagerButton().isSelected()) {
                roleValue = this.view.getManagerButton().getText();
            }

            Sector sectorValue = null;
            ArrayList<Sector> sectors = new ArrayList<>();
            if (this.view.getCashierButton().isSelected()) {
                if (this.view.getHomeAppliancesRadioButton().isSelected()) sectorValue = Sector.HomeAppliances;
                else if (this.view.getMobileDevicesRadioButton().isSelected()) sectorValue = Sector.MobileDevices;
                else if (this.view.getComputersRadioButton().isSelected()) sectorValue = Sector.Computers;
                else if (this.view.getSmallElectronicsRadioButton().isSelected()) sectorValue = Sector.SmallElectronics;
                else if (this.view.getGamingRadioButton().isSelected()) sectorValue = Sector.Gaming;
                else if (this.view.getAccessoriesRadioButton().isSelected()) sectorValue = Sector.Accessories;
                else if (this.view.getCameraRadioButton().isSelected()) sectorValue = Sector.Camera;
            } else if (this.view.getManagerButton().isSelected()) {
                if (this.view.getHomeAppliancesRadioButton().isSelected()) sectors.add(Sector.HomeAppliances);
                if (this.view.getComputersRadioButton().isSelected()) sectors.add(Sector.MobileDevices);
                if (this.view.getSmallElectronicsRadioButton().isSelected()) sectors.add(Sector.Computers);
                if (this.view.getSmallElectronicsRadioButton().isSelected()) sectors.add(Sector.SmallElectronics);
                if (this.view.getGamingRadioButton().isSelected()) sectors.add(Sector.Gaming);
                if (this.view.getAccessoriesRadioButton().isSelected()) sectors.add(Sector.Accessories);
                if (this.view.getCameraRadioButton().isSelected()) sectors.add(Sector.Camera);
            }
            
            try {
            	if(roleValue.equals("Cashier")) {
            		double salaryDouble = Double.parseDouble(salaryValue);
            		((Administrator)this.view.getEmployee()).addEmployee(new Cashier(sectorValue, empNameValue, empSurnameValue, DoBValue, phoneNrValue, emailValue, salaryDouble, addressValue));
            		this.view.showAlert(AlertType.INFORMATION, "Employee Registration", "Cashier added successfully!");
            		this.employeeDAO.getAllEmployees().add(Administrator.getEmployees().get(Administrator.getEmployees().size()-1));
            		this.employeeDAO.writeEmployeeToFile();
            		this.view.getPrimaryEmpStage().close();
            		populateEmployees();
            		this.view.displayEmployeeView();
            	} else if(roleValue.equals("Manager")) {
            		double salaryDouble = Double.parseDouble(salaryValue);
            		((Administrator)this.view.getEmployee()).addEmployee(new Manager(empNameValue, empSurnameValue, DoBValue, phoneNrValue, emailValue, salaryDouble, addressValue, sectors));
            		this.view.showAlert(AlertType.INFORMATION, "Employee Registration", "Manager added successfully!");
            		this.employeeDAO.getAllEmployees().add(Administrator.getEmployees().get(Administrator.getEmployees().size()-1));
            		this.employeeDAO.writeEmployeeToFile();
            		this.view.getPrimaryEmpStage().close();
            		populateEmployees();
            	}
            } catch(NumberFormatException ex) {
            	this.view.showAlert(AlertType.ERROR, "Employee Registration Error", "Salary must be numeric");
        	} catch(Exception ex) {
            	this.view.showAlert(AlertType.ERROR, "Employee Registration Error", ex.getMessage());
            }
        });
	}
	
	private void updateEmployee() {
		this.view.getSaveButton().setOnAction(e -> {
        	HBox phoneNr = (HBox)this.view.getEmployeeDetailsBox().getChildren().get(4);
        	TextField phoneNrField = ((TextField)(phoneNr.getChildren().get(1)));
        	HBox email = (HBox)this.view.getEmployeeDetailsBox().getChildren().get(5);
        	TextField emailField = ((TextField)(email.getChildren().get(1)));
        	HBox address = (HBox)this.view.getEmployeeDetailsBox().getChildren().get(6);
        	TextField addressField = ((TextField)(address.getChildren().get(1)));
        	HBox salary = (HBox)this.view.getEmployeeDetailsBox().getChildren().get(8);
        	TextField salaryField = ((TextField)(salary.getChildren().get(1)));
        	HBox sector = (HBox)this.view.getEmployeeDetailsBox().getChildren().get(9);
        	TextField sectorField = ((TextField)(sector.getChildren().get(1)));
        	if(phoneNrField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
        	   addressField.getText().trim().isEmpty() || salaryField.getText().trim().isEmpty() || sectorField.getText().trim().isEmpty()) {
        		this.view.showAlert(AlertType.ERROR, "Personal Information", "No field can be empty!");
        	} else {
        		String sectorText = sectorField.getText();
        		ArrayList<String> validSectors = new ArrayList<>();
        		for(Sector sectorValue: Sector.values()) {
        			validSectors.add(sectorValue.toString());
        		}

        		if (this.view.getSelectedEmp() instanceof Cashier) {
        		   if (!validSectors.contains(sectorText.trim())) {
        			   this.view.showAlert(AlertType.ERROR, "Editing Cashier", "Invalid Sector Entered");
        		   } else{
        			    try {
        			    	String salaryText = salaryField.getText();
    			    		if(salaryField.getText().charAt(0) == '$') {
    			    			salaryText = salaryField.getText().substring(1);
    			    		}
    			    		this.view.getSelectedEmp().setSalary(Double.parseDouble(salaryText));
    			    		this.view.getSelectedEmp().setPhoneNumber(phoneNrField.getText());
    			    		this.view.getSelectedEmp().setEmail(emailField.getText());
    			    		this.view.getSelectedEmp().setAddress(addressField.getText());
    				    	((Cashier)this.view.getSelectedEmp()).setPermCreateBill(this.view.getCreateBillCheckbox().isSelected());
    				    	((Cashier)this.view.getSelectedEmp()).setPermShowTodayBill(this.view.getShowTodaysBillsCheckbox().isSelected());
    				    	((Cashier)this.view.getSelectedEmp()).setPermShowTotalOfAllBills(this.view.getTotalOfAllBillsCheckbox().isSelected());
	        		    	for(Sector sectorValue: Sector.values()) {
	        		    		if(sectorValue.toString().equals(sectorText)) {
	        		    			((Cashier)this.view.getSelectedEmp()).setSector(sectorValue);
	        		    			break;
	        		    		}
	        		    	}
	        		    	this.view.showAlert(AlertType.INFORMATION, "Employee", "Changes saved");
	        		    	this.employeeDAO.writeEmployeeToFile();
	        		    	populateEmployees();
    				    	this.view.displayEmployeeView();
        			    } catch(Exception ex) {
				    		this.view.showAlert(AlertType.ERROR, "Personal Information", ex.getMessage());
				    	}
        		    }
        		} else if (this.view.getSelectedEmp() instanceof Manager) {
        			String[] sectors = sectorText.split(",");
        			boolean marker = true;
    		        for (String sectorValue : sectors) {
    		            String trimmedSector = sectorValue.trim();
    		            if (!validSectors.contains(trimmedSector)) {
    		                this.view.showAlert(AlertType.ERROR, "Editing Manager", "Invalid Sectors Entered");
    		                marker = false;
    		                break;
    		            }
        		    }
    		        if(marker) {
		    			try {
	    		        	ArrayList<Sector> managerSector = new ArrayList<>();
			    			for(String s: sectors) {
			    				for(Sector sectorValue: Sector.values()) {
			    					if(s.trim().equals(sectorValue.toString())) {
			    						managerSector.add(sectorValue);
		        		    			break;
			    					}
			    				}
			    			}
			    			String salaryText = salaryField.getText();
				    		if(salaryField.getText().charAt(0) == '$') {
				    			salaryText = salaryField.getText().substring(1);
				    		}
				    		this.view.getSelectedEmp().setSalary(Double.parseDouble(salaryText));
				    		((Manager)this.view.getSelectedEmp()).setSectorList(managerSector);
				    		this.view.getSelectedEmp().setPhoneNumber(phoneNrField.getText());
				    		this.view.getSelectedEmp().setEmail(emailField.getText());
				    		this.view.getSelectedEmp().setAddress(addressField.getText());
    				    	((Manager)this.view.getSelectedEmp()).setPermShowCashierPerformance(this.view.getShowCashierPerformanceCheckbox().isSelected());
    				    	((Manager)this.view.getSelectedEmp()).setPermAddItemInventory(this.view.getAddItemInventoryCheckbox().isSelected());
    				    	((Manager)this.view.getSelectedEmp()).setPermCreateCategory(this.view.getCreateCategoryCheckbox().isSelected());
    				    	((Manager)this.view.getSelectedEmp()).setPermShowStatisticsAboutItems(this.view.showStatisticsAboutItemsCheckbox().isSelected());
    				    	this.view.showAlert(AlertType.INFORMATION, "Employee", "Changes saved");
    				    	this.employeeDAO.writeEmployeeToFile();
	        		    	populateEmployees();
    				    	this.view.displayEmployeeView();
		    			} catch(Exception ex) {
				    		this.view.showAlert(AlertType.ERROR, "Personal Information", ex.getMessage());
				    	}
    		        }
    		    }
        	}
        });
	}
	
	private void setRemoveEmployee() {
		this.view.getRemoveButton().setOnAction(e -> {
        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete employee?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
            	((Administrator)this.view.getEmployee()).removeEmployee(this.view.getSelectedEmp());
            	this.employeeDAO.writeEmployeeToFile();
            	this.view.getEmployees().remove(this.view.getSelectedEmp());
            	this.populateEmployees();
            	this.view.displayEmployeeView();
            } else {
                alert.close();
            }
        });
	}
	
	private void setChangePasswordUser() {
		this.view.getChangePasswordButton().setOnAction(e -> {
            String newPassword = this.view.getNewUserPasswordField().getText();
            String confirmNewPassword = this.view.getConfirmUserNewPasswordField().getText();

            if (newPassword.trim().isEmpty() || confirmNewPassword.trim().isEmpty()) {
                this.view.showAlert(Alert.AlertType.WARNING, "Empty Fields", "All fields must be filled!");
            } else if (!newPassword.equals(confirmNewPassword)) {
                this.view.showAlert(Alert.AlertType.ERROR, "Password Mismatch", "New password and confirmation do not match.");
            } else {
            	(this.view.getSelectedEmp()).setPassword(newPassword);
            	this.view.showAlert(Alert.AlertType.INFORMATION, "Change Password", "Password changes successfully!");
            	this.view.getPopupUserPasswordStage().close();
                employeeDAO.writeEmployeeToFile();
                populateEmployees();
                this.view.displayEmployeeView();
            }
      });
	}
	
	private void populateSuppliers() {
		this.view.getFlowPane().getChildren().clear();
		this.view.getSuppliers().clear();
        for (Employee emp: Administrator.getEmployees()) {
        	if(emp instanceof Manager) {
        		for(Supplier s: ((Manager)emp).getSupplierList()) {
        			this.view.getSuppliers().add(s);
        		}
        	}
        }
		for(Supplier s: this.view.getSuppliers()) {
			Button supplierButton = this.view.createSupplierCard(s, this.view.getSupplierButtons(), this.view.getPane(), this.view.getSuppliers(), this.view.getIsExpanded());
        	this.view.getFlowPane().getChildren().add(supplierButton);
		}	
	}
	
	private void searchSupplier() {
		this.view.getSearchField().setOnKeyTyped(e -> {
            String searchText = this.view.getSearchField().getText();
            this.view.getFlowPane().getChildren().clear();
            this.view.getFlowPane().getChildren().removeAll();
            for (Supplier supplier: this.view.getSuppliers()) {
            	if(supplier.getSupplierName().toLowerCase().contains(searchText.toLowerCase()) || searchText.trim().isEmpty()) {
            		Button supplierButton = this.view.createSupplierCard(supplier, this.view.getSupplierButtons(), this.view.getPane(), this.view.getSuppliers(), this.view.getIsExpanded());
                	this.view.getFlowPane().getChildren().add(supplierButton);
            	}
            }
            if(this.view.getFlowPane().getChildren().isEmpty()) {
            	Text noResultsText = new Text("No results found!");
                noResultsText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                noResultsText.setFill(Color.DARKGRAY);

                StackPane centeredTextContainer = new StackPane(noResultsText);
                this.view.getFlowPane().getChildren().add(centeredTextContainer);
            }
        });
	}
	
	private void setAddSupplier() {
		for(Employee emp: this.employeeDAO.getAllEmployees()) {
        	if(emp instanceof Manager)
        	this.view.getManagerComboBox().getItems().add(emp.getEmployeeName());
        }
		this.view.getManagerComboBox().setPromptText("Select an option");
        
		this.view.getSupplierSubmitButton().setOnAction(e -> {
            String supplierNameValue = view.getSupplierNameField().getText();
            String supplierPhoneNrValue = view.getSupplierPhoneNrField().getText();
            String supplierAddressValue = view.getSupplierAddressField().getText();
            String[] itemsOfferedArray = view.getItemsOfferedField().getText().split(",");
            String managerSelected = view.getManagerComboBox().getValue();
            
            try {
            	Manager selectedManager = null;
            	for(Employee emp: employeeDAO.getAllEmployees()) {
            		if(emp instanceof Manager && emp.getEmployeeName().equals(managerSelected)) {
            			selectedManager = (Manager)emp;
            		}
            	}
        		Administrator.addSupplier(new Supplier(supplierNameValue, supplierPhoneNrValue, supplierAddressValue, new ArrayList<>(Arrays.asList(itemsOfferedArray))), selectedManager);
        		this.employeeDAO.writeEmployeeToFile();
        		this.view.showAlert(AlertType.INFORMATION, "Supplier Registration", "Supplier added successfully!");
        		this.view.getPrimaryStage().close();
        		populateSuppliers();
        		this.view.displaySupplierView();
            } catch(Exception ex) {
            	this.view.showAlert(AlertType.ERROR, "Supplier Registration Error", ex.getMessage());
            }
        });
	}
	
	private void setUpdateSupplier() {		
		this.view.getSaveSupplierButton().setOnAction(e -> {
        	HBox phoneNr = (HBox)this.view.getDetailsBox().getChildren().get(2);
        	TextField phoneNrField = ((TextField)(phoneNr.getChildren().get(1)));
        	HBox address = (HBox)this.view.getDetailsBox().getChildren().get(3);
        	TextField addressField = ((TextField)(address.getChildren().get(1)));
        	HBox items = (HBox)this.view.getDetailsBox().getChildren().get(4);
        	TextField itemsField = ((TextField)(items.getChildren().get(1)));
        	if(phoneNrField.getText().trim().isEmpty() || itemsField.getText().trim().isEmpty() ||
        	   addressField.getText().trim().isEmpty()) {
        		this.view.showAlert(AlertType.ERROR, "Personal Information", "No field can be empty!");
        	} else {
		    	try {
		    		String[] itemsOfferedList = itemsField.getText().split(",");
			    	this.view.getSelectedSupplier().setSupplierPhoneNr(phoneNrField.getText());
			    	this.view.getSelectedSupplier().setSupplierAddress(addressField.getText());
			    	this.view.getSelectedSupplier().setItemsOffered(new ArrayList<String>(Arrays.asList(itemsOfferedList)));
			    	this.employeeDAO.writeEmployeeToFile();
			    	this.view.showAlert(AlertType.INFORMATION, "Supplier", "Changes saved");
			    	this.view.displaySupplierView();
		    	} catch(Exception ex) {
		    		this.view.showAlert(AlertType.ERROR, "Personal Information", ex.getMessage());
		    	}
            }
        });
	}
	
	private void setRemoveSupplier() {
		this.view.getRemoveSupplierButton().setOnAction(e -> {
        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete supplier?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");

            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == yesButton) {
            	((Administrator)this.view.getEmployee()).removeSupplier(this.view.getSelectedSupplier());
            	this.employeeDAO.writeEmployeeToFile();
            	populateSuppliers();
            	this.view.displaySupplierView();
            } else {
                alert.close();
            }
        });
	}
	
	private void setSaveProfile() {
		this.view.getSaveProfileButton().setOnAction(e -> {
        	HBox phoneNr = (HBox)this.view.getProfileDetailsBox().getChildren().get(4);
        	TextField phoneNrField = ((TextField)(phoneNr.getChildren().get(1)));
        	HBox email = (HBox)this.view.getProfileDetailsBox().getChildren().get(5);
        	TextField emailField = ((TextField)(email.getChildren().get(1)));
        	HBox address = (HBox)this.view.getProfileDetailsBox().getChildren().get(6);
        	TextField addressField = ((TextField)(address.getChildren().get(1)));
        	HBox salary = (HBox)this.view.getProfileDetailsBox().getChildren().get(8);
        	TextField salaryField = ((TextField)(salary.getChildren().get(1)));
        	if(phoneNrField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
        	   addressField.getText().trim().isEmpty() || salaryField.getText().trim().isEmpty()) {
        		this.view.showAlert(AlertType.ERROR, "Personal Information", "No field can be empty!");
        	} else {
		    	try {
		    		String salaryText = salaryField.getText();
		    		if(salaryField.getText().charAt(0) == '$') {
		    			salaryText = salaryField.getText().substring(1);
		    		}
			    	((Administrator)this.view.getEmployee()).setPhoneNumber(phoneNrField.getText());
        			((Administrator)this.view.getEmployee()).setSalary(Double.parseDouble(salaryText));
			    	((Administrator)this.view.getEmployee()).setEmail(emailField.getText());
			    	((Administrator)this.view.getEmployee()).setAddress(addressField.getText());
			    	this.employeeDAO.writeEmployeeToFile();
			    	this.view.showAlert(AlertType.INFORMATION, "Employee", "Changes saved");
			    	this.view.displayProfileView();
		    	} catch(Exception ex) {
		    		this.view.showAlert(AlertType.ERROR, "Personal Information", ex.getMessage());
		    	}
        	}
        });
	}
	
	private void setChangePasswordAdmin() {
		 this.view.getChangePasswordSubmitButton().setOnAction(e -> {
	            String newPassword = this.view.getNewPasswordField().getText();
	            String confirmNewPassword = this.view.getConfirmNewPasswordField().getText();

	            if (newPassword.trim().isEmpty() || confirmNewPassword.trim().isEmpty()) {
	                this.view.showAlert(Alert.AlertType.WARNING, "Empty Fields", "All fields must be filled!");
	            } else if (!newPassword.equals(confirmNewPassword)) {
	                this.view.showAlert(Alert.AlertType.ERROR, "Password Mismatch", "New password and confirmation do not match.");
	            } else {
	            	((Administrator)this.view.getEmployee()).setPassword(newPassword);
	            	this.view.showAlert(Alert.AlertType.INFORMATION, "Change Password", "Password changes successfully!");
	            	this.view.getPopupPasswordStage().close();
	                employeeDAO.writeEmployeeToFile();
	                this.view.displayProfileView();
	            }
	      });
	 }
}
package controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import dao.EmployeeDAO;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Cashier;
import model.Employee;
import model.Inventory;
import model.Item;
import model.Manager;
import model.Supplier;
import view.ManagerView;

public class ManagerController {

	private final ManagerView view;
	private final EmployeeDAO employeeDAO;
	
	public ManagerView getView() {
		return view;
	}
	
	public ManagerController(Stage stage, Employee employee) {

		this.view = new ManagerView(stage, employee);
		employeeDAO = new EmployeeDAO();

		setSidebarOnClickActions();
		populateTable();
		searchTable();
		populateCashiers();
		searchCashier();
		submitInventory();
		setOnEditCommitEvent();
		setOtherActions();
		statisticsOptions();
	}
	
	private void setSidebarOnClickActions() {
		this.view.getCashierPerformanceButton().setOnAction(event -> this.view.displayCashierPerformanceView());
		this.view.getInventoryButton().setOnAction(event -> this.view.displayInventoryView());
		this.view.getStatisticsButton().setOnAction(event -> this.view.displayStatisticsView());
		this.view.getProfileButton().setOnAction(event -> this.view.displayProfileView());
		this.view.getLogoutButton().setOnAction(event -> this.view.navigateToLogin());
	}
	
	private void populateCashiers() {
        for (Employee emp: this.view.getCashiers()) {
	        	Button empButton = this.view.createCashierCard((Cashier)emp, this.view.getEmpButtons(), this.view.getPane(), this.view.getCashiers(), this.view.getIsExpanded());
	        	this.view.getFlowPane().getChildren().add(empButton);
        }
	}
	
	private void searchCashier() {
		this.view.getSearchField().setOnKeyTyped(e -> {
            String searchText = this.view.getSearchField().getText();
            this.view.getFlowPane().getChildren().clear();
            for (Employee emp: this.view.getCashiers()) {
            	if((((Manager)this.view.getEmployee()).getSectorList().contains(((Cashier)emp).getSector()) &&  
            			(emp.getEmployeeName().toLowerCase().contains(searchText.toLowerCase()) || searchText.trim().isEmpty()))) {
            		Button empButton = this.view.createCashierCard((Cashier)emp, this.view.getEmpButtons(), this.view.getPane(), this.view.getCashiers(), this.view.getIsExpanded());
                	this.view.getFlowPane().getChildren().add(empButton);
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
	
	private void searchTable() {
		this.view.getSearchTableField().setOnKeyTyped(e -> {
			this.view.getTableItems().clear();
		    this.view.getTableView().getItems().clear();
		    String searchText = this.view.getSearchTableField().getText();
		    for (int i = 0; i < Inventory.getItems().size(); i++) {
            	if(Inventory.getItems().get(i).getName().toLowerCase().contains(searchText.toLowerCase()) || searchText.trim().isEmpty() || searchText == null) {
			        Item currentItem = Inventory.getItems().get(i);
			        boolean itemExists = false;
			        for (ManagerView.ItemData itemData : this.view.getTableItems()) {
			            if (itemData.getName().equals(currentItem.getName())) {
			                itemExists = true;
			                break;
			            }
			        }
			        if (!itemExists) {
			            ManagerView.ItemData itemData = new ManagerView.ItemData(currentItem, Inventory.getQuantity().get(i));
			            this.view.getTableItems().add(itemData);
			        } else {
			            ManagerView.ItemData newItemData = new ManagerView.ItemData(currentItem, Inventory.getQuantity().get(i));
			            int index = -1;
			            for (int j = 0; j < this.view.getTableItems().size(); j++) {
			                if (this.view.getTableItems().get(j).getName().equals(currentItem.getName())) {
			                    index = j;
			                }
			            }
			            if (index >= 0) {
			                this.view.getTableItems().add(index + 1, newItemData);
			            }
			        }
		    	}
		    }
		    this.view.updateTable();
        });
    }
	
	private void populateTable() {
	    this.view.getTableItems().clear();
	    this.view.getTableView().getItems().clear();
	    for (int i = 0; i < Inventory.getItems().size(); i++) {
	        Item currentItem = Inventory.getItems().get(i);
	        boolean itemExists = false;
	        for (ManagerView.ItemData itemData : this.view.getTableItems()) {
	            if (itemData.getName().equals(currentItem.getName())) {
	                itemExists = true;
	                break;
	            }
	        }
	        if (!itemExists) {
	            ManagerView.ItemData itemData = new ManagerView.ItemData(currentItem, Inventory.getQuantity().get(i));
	            this.view.getTableItems().add(itemData);
	        } else {
	            ManagerView.ItemData newItemData = new ManagerView.ItemData(currentItem, Inventory.getQuantity().get(i));
	            int index = -1;
	            for (int j = 0; j < this.view.getTableItems().size(); j++) {
	                if (this.view.getTableItems().get(j).getName().equals(currentItem.getName())) {
	                    index = j;
	                }
	            }	            
	            if (index >= 0) {
	                this.view.getTableItems().add(index + 1, newItemData);
	            }
	        }
	    }
	    this.view.updateTable();
	}
	
	private void submitInventory() {
		this.view.getSubmitButton().setOnAction(event -> {
        	if(this.view.getNameField().getText().trim().isEmpty() || (!this.view.getNewCategoryCheckBox().isSelected() && this.view.getCategoryComboBox().getValue().isEmpty()) 
        			|| (this.view.getNewCategoryCheckBox().isSelected() && this.view.getNewCategoryField().getText().trim().isEmpty()) || this.view.getSupplierComboBox().getValue().isEmpty()
        			|| this.view.getPurchaseDatePicker().equals(null) || this.view.getPurchasePriceField().getText().trim().isEmpty() || this.view.getSellingPriceField().getText().trim().isEmpty()
        			|| this.view.getQuantityField().getText().trim().isEmpty()) {
        		this.view.showAlert(AlertType.ERROR, "Inventory", "No fields can be empty!");
        	} else {
        		try {
		        String name = this.view.getNameField().getText();
		        name = name.substring(0, 1).toUpperCase() + name.substring(1);
		        String category = this.view.getCategoryComboBox().getValue();
		        if (this.view.getNewCategoryCheckBox().isSelected()) {
		            category = this.view.getNewCategoryField().getText();
		        }
		        String newCategory = this.view.getNewCategoryField().getText();
		        String supplierName = this.view.getSupplierComboBox().getValue();
		        LocalDate purchaseDateValue = this.view.getPurchaseDatePicker().getValue();
		        Date purchaseDate = Date.valueOf(purchaseDateValue);
		        double purchasePrice = Double.parseDouble(this.view.getPurchasePriceField().getText());
		        double sellingPrice = Double.parseDouble(this.view.getSellingPriceField().getText());
		        int quantity = Integer.parseInt(this.view.getQuantityField().getText());
		        
		        Supplier selectedSupplier = null;
		        for (Supplier supplier : ((Manager) this.view.getEmployee()).getSupplierList()) {
		            if (supplier.getSupplierName().equals(supplierName)) {
		                selectedSupplier = supplier;
		                break;
		            }
		        }
		        if (purchaseDateValue.isAfter(LocalDate.now())) {
        			this.view.showAlert(AlertType.ERROR, "Inventory", "Purchase price cannot be later than today!");
		        } else if(!selectedSupplier.getItemsOffered().contains(name)) {
        			this.view.showAlert(AlertType.ERROR, "Inventory", "The supplier you selected doesn't offer this product!");
        		} else if(checkInventoryForItems(name, sellingPrice)) {
			        if(!this.view.getNewCategoryCheckBox().isSelected()) {
				        	Inventory.addItemtoInventory(name, category, selectedSupplier, purchaseDate, purchasePrice, sellingPrice, quantity);
			        	}
			        else {
				        	Inventory.addItemtoInventory(name, newCategory, selectedSupplier, purchaseDate, purchasePrice, sellingPrice, quantity);
			        	}
		        	this.employeeDAO.writeInventoryToFile();
        			this.view.showAlert(AlertType.INFORMATION, "Inventory", "Item saved successfully!");
			        this.view.getNameField().clear();
			        this.view.getCategoryComboBox().setValue(null);
		            this.view.getNewCategoryField().clear();
		            this.view.getSupplierComboBox().setValue(null);
		            this.view.getPurchaseDatePicker().setValue(null);
		            this.view.getPurchasePriceField().clear();
		            this.view.getSellingPriceField().clear();
		            this.view.getQuantityField().clear();
		            populateTable();
			    }
		        
        		} catch(NumberFormatException e) {
        			this.view.showAlert(AlertType.ERROR, "Inventory", "Prices have to be numeric!");
        		} catch(Exception e) {
        			this.view.showAlert(AlertType.ERROR, "Inventory", e.getMessage());
        		}
        	}
        });
	}
	
	private void setOnEditCommitEvent() {
		this.view.getSellingPriceColumn().setOnEditCommit(e -> {
			ManagerView.ItemData itemData = e.getRowValue();
			for(ManagerView.ItemData i: this.view.getTableItems()) {
				Item item = i.getItem();
				if(item.getName().equals(itemData.getName())) {
					i.sellingPriceProperty().set(e.getNewValue());
					item.setSellingPrice(e.getNewValue());
				}
			}
			this.view.updateTable();
        	this.employeeDAO.writeInventoryToFile();
		});
	}
	
	private boolean checkInventoryForItems(String name, double sellingPrice) {
		boolean marker = false;
		double currentPrice = 0;
		for(ManagerView.ItemData i: this.view.getTableItems()) {
			if(i.getName().toLowerCase().equals(name.toLowerCase()) && i.getSellingPrice() != sellingPrice) {
				marker = true;
				currentPrice = i.getSellingPrice();
				break;
			}
		}
		
		if(marker) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirm");
	        alert.setHeaderText(null);
	        alert.setContentText("This product is already in inventory with price $" + currentPrice + ".\n" + "Clicking yes will change the price for this product in inventory.\n" + "Are you sure?");
	        ButtonType yesButton = new ButtonType("Yes");
	        ButtonType noButton = new ButtonType("No");
	        alert.getButtonTypes().setAll(yesButton, noButton);
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == yesButton) {
    			for(ManagerView.ItemData i: this.view.getTableItems()) {
    				Item item = i.getItem();
    				if(item.getName().equals(name)) {
    					i.sellingPriceProperty().set(sellingPrice);
    					item.setSellingPrice(sellingPrice);
    				}
    			}
    			return true;
	        } else {
	            alert.close();
	            return false;
	        }
		}
		return true;
	}
	
	public void setOtherActions() {
		this.view.getChangePasswordSubmitButton().setOnAction(e -> {
            String currentPassword = this.view.getCurrentPasswordField().getText();
            String newPassword = this.view.getNewPasswordField().getText();
            String confirmNewPassword = this.view.getConfirmNewPasswordField().getText();

            if (currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmNewPassword.trim().isEmpty()) {
                this.view.showAlert(Alert.AlertType.WARNING, "Empty Fields", "All fields must be filled!");
            } else if (!newPassword.equals(confirmNewPassword)) {
                this.view.showAlert(Alert.AlertType.ERROR, "Password Mismatch", "New password and confirmation do not match.");
            } else if (currentPassword.equals(((Manager)this.view.getEmployee()).getPassword())) {
                this.view.showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
                this.view.getEmployee().setPassword(newPassword);
                this.view.getPopupStage().close();
                this.view.displayProfileView();
                this.employeeDAO.writeEmployeeToFile();
            } else {
            	this.view.showAlert(Alert.AlertType.ERROR, "Wrong password", "The current password you entered is wrong.");
            }
        });
	}
	
	public void statisticsOptions() {
		this.view.getSubmitOptionButton().setOnAction(e -> {
            String selectedOption = this.view.getOptionComboBox().getValue();

            if (selectedOption == null) {
                view.showAlert(AlertType.ERROR, "Statistics", "Option cannot be null.");
            } else {
                CategoryAxis xAxisPurchased = new CategoryAxis();
                NumberAxis yAxisPurchased = new NumberAxis();
                xAxisPurchased.setLabel("Date");
                yAxisPurchased.setLabel("Cost (in $)");

                BarChart<String, Number> barChartPurchased = new BarChart<>(xAxisPurchased, yAxisPurchased);
                barChartPurchased.setLegendVisible(false);
                XYChart.Series<String, Number> seriesPurchased = new XYChart.Series<>();

                CategoryAxis xAxisSold = new CategoryAxis();
                NumberAxis yAxisSold = new NumberAxis();
                xAxisSold.setLabel("Date");
                yAxisSold.setLabel("Income (in $)");

                BarChart<String, Number> barChartSold = new BarChart<>(xAxisSold, yAxisSold);
                barChartSold.setLegendVisible(false);
                barChartSold.setTitle("Items Sold");
                XYChart.Series<String, Number> seriesSold = new XYChart.Series<>();
                
                this.view.getChartPane().getChildren().clear();
                
                double purchasedCost = ((Manager)view.getEmployee()).showStatisticsAboutItemsPurchased(selectedOption);
                double soldCost = ((Manager) view.getEmployee()).showStatisticsAboutItemsSold(selectedOption);

                barChartPurchased.setTitle("Total Purchase Cost: $" + purchasedCost);
                seriesPurchased.getData().add(new XYChart.Data<>(selectedOption, purchasedCost));
                barChartPurchased.getData().add(seriesPurchased);
                seriesPurchased.getData().get(0).getNode().setStyle("-fx-bar-fill: #ed0909;");

                barChartSold.setTitle("Total Revenue: $" + soldCost);
                seriesSold.getData().add(new XYChart.Data<>(selectedOption, soldCost));
                barChartSold.getData().add(seriesSold);
                seriesSold.getData().get(0).getNode().setStyle("-fx-bar-fill: #048517;");

                this.view.getChartPane().getChildren().addAll(barChartPurchased, barChartSold);
            }
        });
	}
}

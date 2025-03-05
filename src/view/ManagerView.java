package view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import controller.LoginController;

import java.sql.Date;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import model.Administrator;
import model.Cashier;
import model.Employee;
import model.Inventory;
import model.Item;
import model.Manager;
import model.Sector;
import model.Supplier;

@SuppressWarnings({ "unchecked"})
public class ManagerView extends BorderPane {

	private static final Button cashierPerformanceButton = new Button("Cashier Performance");
    private static final Button inventoryButton = new Button("Inventory");
    private static final Button statisticsButton = new Button("Statistics");
    private static final Button profileButton = new Button("Profile");
    private static final Button logoutButton = new Button("Logout");
    
    public Button getCashierPerformanceButton() { return cashierPerformanceButton; }
    public Button getInventoryButton() { return inventoryButton; }
    public Button getStatisticsButton() { return statisticsButton; }
    public Button getProfileButton() { return profileButton; }
    public Button getLogoutButton() { return logoutButton; }
    
    private TableView<ItemData> tableView = new TableView<>();
    private ArrayList<ItemData> tableItems = new ArrayList<>();
    TableColumn<ItemData, Double> sellingPriceColumn = new TableColumn<>("Selling Price ($)");
    
    TextField searchTableField = new TextField();
    public TextField getSearchTableField() { return this.searchTableField; }
    
    public TableView<ItemData> getTableView() { return tableView; }
    public ArrayList<ItemData> getTableItems() { return tableItems; }
    public TableColumn<ItemData, Double> getSellingPriceColumn() { return sellingPriceColumn; }

    private final Stage stage;
    private final Employee employee;
    
    public Employee getEmployee() { return employee; }
    
    public ManagerView(Stage stage, Employee employee) {
        this.stage = stage;
        this.employee = employee;
        setupView();
    }

    private void setupView() {
        BorderPane sidebar = createSidebar();
        this.setLeft(sidebar);
        displayCashierPerformanceView();
    }

    private BorderPane createSidebar() {
    	ImageView logoView = new ImageView(new Image("images/logosidebar.png"));
    	logoView.setFitWidth(140);
    	logoView.setPreserveRatio(true);
    	StackPane pane = new StackPane(logoView);
    	pane.setPadding(new Insets(20, 0, 20, 0));

    	String buttonStyle = "-fx-background-color: #1A1A1A; " +
    	                     "-fx-text-fill: white; " +
    	                     "-fx-font-size: 14px; " +
    	                     "-fx-padding: 10px 0; " +
    	                     "-fx-background-radius: 0; " +
    	                     "-fx-border-color: transparent; " +
    	                     "-fx-pref-width: 200px; " +
    	                     "-fx-pref-height: 50px;";

    	String buttonHoverStyle = "-fx-background-color: #333333; -fx-border-color: #444444;";

    	String profileStyle = "-fx-background-color: #0056A6; " +
    	                            "-fx-text-fill: white; " +
    	                            "-fx-font-size: 14px; " +
    	                            "-fx-padding: 10px 0; " +
    	                            "-fx-background-radius: 5px; " +
    	                            "-fx-pref-width: 180px;";
    	
    	String profileHoverStyle = "-fx-background-color: #483d8b";

    	String logoutStyle = "-fx-background-color: #A80000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 0; -fx-background-radius: 5px; -fx-pref-width: 180px;";
    	
    	String logoutHoverStyle = "-fx-background-color: #960303;";
    	
    	configureButton(cashierPerformanceButton, buttonStyle, buttonHoverStyle);
    	configureButton(inventoryButton, buttonStyle, buttonHoverStyle);
    	configureButton(statisticsButton, buttonStyle, buttonHoverStyle);
    	configureButton(profileButton, profileStyle, profileHoverStyle);
    	configureButton(logoutButton, logoutStyle, logoutHoverStyle);

    	ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(cashierPerformanceButton);
        buttons.add(inventoryButton);
        buttons.add(statisticsButton);
    	
      	ImageView logoutIcon = new ImageView(new Image("images/logout.png"));
    	logoutIcon.setFitWidth(16);
    	logoutIcon.setFitHeight(16);
    	logoutIcon.setPreserveRatio(true);
    	logoutButton.setGraphic(logoutIcon);
    	
    	VBox buttonContainer = new VBox(cashierPerformanceButton, inventoryButton, statisticsButton);
    	buttonContainer.setSpacing(0);
    	buttonContainer.setAlignment(Pos.TOP_CENTER);

    	VBox bottomContainer = new VBox(10, profileButton, logoutButton);
    	bottomContainer.setAlignment(Pos.BOTTOM_CENTER);
    	bottomContainer.setPadding(new Insets(10, 0, 10, 0));

    	BorderPane sidebar = new BorderPane();
    	sidebar.setStyle("-fx-background-color: #2E2E2E; -fx-padding: 0;");
    	sidebar.setPrefWidth(200);
    	sidebar.setTop(pane);
    	BorderPane.setAlignment(logoView, Pos.CENTER);
    	sidebar.setCenter(buttonContainer);
    	sidebar.setBottom(bottomContainer);
    	
    	return sidebar;
    }
    
    
	public void displayInventoryView() {
		if(!((Manager)employee).getPermAddItemInventory()) {
    		showAlert(AlertType.ERROR, "Permission Error", "You don't have permission to view the inventory!");
    		return;
    	}
    	this.setRight(null);
    	
    	tableView.setEditable(true);
    	
    	searchTableField.setPromptText("Search...");
    	searchTableField.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-padding: 5px;");
    	searchTableField.setPrefWidth(200);
    	searchTableField.setMinWidth(200);
    	searchTableField.setMaxWidth(200);
        
        HBox searchBarContainer = new HBox(searchTableField);
        searchBarContainer.setSpacing(10);
        searchBarContainer.setPadding(new Insets(10));
        searchBarContainer.setAlignment(Pos.TOP_CENTER);
        searchBarContainer.setStyle("-fx-border-radius: 10px;");
    	
        TableColumn<ItemData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<ItemData, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<ItemData, String> supplierColumn = new TableColumn<>("Supplier Name");
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        TableColumn<ItemData, String> purchaseDateColumn = new TableColumn<>("Purchase Date");
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));

        TableColumn<ItemData, Double> purchasePriceColumn = new TableColumn<>("Purchase Price ($)");
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        sellingPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        TableColumn<ItemData, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        nameColumn.setPrefWidth(130);
        categoryColumn.setPrefWidth(130);
        supplierColumn.setPrefWidth(130);
        purchaseDateColumn.setPrefWidth(198);
        purchasePriceColumn.setPrefWidth(130);
        sellingPriceColumn.setPrefWidth(130);
        quantityColumn.setPrefWidth(130);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        
        if(tableView.getColumns().isEmpty()) {
            tableView.getColumns().addAll(nameColumn, categoryColumn, supplierColumn, purchaseDateColumn, purchasePriceColumn, sellingPriceColumn, quantityColumn);
        }

        tableView.setPrefSize(800, 300);
        
        VBox formBox = createFormBox();
        formBox.setPadding(new Insets(40));
        
        VBox box = new VBox(searchBarContainer, tableView, formBox);
        box.setPadding(new Insets(10));

        this.setCenter(box);
        
        String notification = Inventory.notifyManagerOnLowItemStock() + Inventory.notifyManagerOnLowCategoryStock();
        if(!notification.trim().isEmpty()) {
        	showAlert(AlertType.INFORMATION, "Inventory", notification);
        }
    }
    
    private TextField nameField = new TextField();
    private ComboBox<String> categoryComboBox = new ComboBox<>();
    private ComboBox<String> supplierComboBox = new ComboBox<>();
    private DatePicker purchaseDatePicker = new DatePicker();
    private TextField purchasePriceField = new TextField();
    private TextField sellingPriceField = new TextField();
    private TextField quantityField = new TextField();
    private TextField newCategoryField = new TextField();
    private CheckBox newCategoryCheckBox = new CheckBox("New ?");
    private Button submitButton = new Button("Add Item");

    public TextField getNameField() { return this.nameField; }
    public ComboBox<String> getCategoryComboBox() { return this.categoryComboBox; }
    public ComboBox<String> getSupplierComboBox() { return this.supplierComboBox; }
    public DatePicker getPurchaseDatePicker() { return this.purchaseDatePicker;}
    public TextField getPurchasePriceField() { return this.purchasePriceField; }
    public TextField getSellingPriceField() { return this.sellingPriceField; }
    public TextField getQuantityField() { return this.quantityField; }
    public TextField getNewCategoryField() { return this.newCategoryField; }
    public CheckBox getNewCategoryCheckBox() { return this.newCategoryCheckBox; }
    public Button getSubmitButton() { return this.submitButton; }

    public VBox createFormBox() {
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        formBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10); 
        grid.setPadding(new Insets(10));
        
        String fieldStyle = "-fx-border-radius: 5px; -fx-padding: 5px; -fx-font-size: 12px;";

        Label nameLabel = new Label("Name: ");
        nameLabel.setStyle("-fx-font-weight: bold;");
        nameField.setStyle(fieldStyle);

        Label categoryLabel = new Label("Category: ");
        categoryLabel.setStyle("-fx-font-weight: bold;");
        categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Inventory.getCategory());
        categoryComboBox.setStyle("-fx-border-radius: 5px; -fx-font-size: 12px;");
        categoryComboBox.setPrefWidth(150);

        Label supplierLabel = new Label("Supplier: ");
        supplierComboBox = new ComboBox<>();
        supplierLabel.setStyle("-fx-font-weight: bold;");
        for (Supplier supplier : ((Manager) employee).getSupplierList()) {
            supplierComboBox.getItems().add(supplier.getSupplierName());
        }
        supplierComboBox.setStyle("-fx-border-radius: 5px; -fx-font-size: 12px;");
        supplierComboBox.setPrefWidth(150);

        Label purchaseDateLabel = new Label("Purchase Date: ");
        purchaseDateLabel.setStyle("-fx-font-weight: bold;");
        purchaseDatePicker.setStyle(fieldStyle);
        purchaseDatePicker.setPrefWidth(150);

        Label purchasePriceLabel = new Label("Purchase Price: ");
        purchasePriceLabel.setStyle("-fx-font-weight: bold;");
        purchasePriceField.setStyle(fieldStyle);

        Label sellingPriceLabel = new Label("Selling Price: ");
        sellingPriceLabel.setStyle("-fx-font-weight: bold;");
        sellingPriceField.setStyle(fieldStyle);

        Label quantityLabel = new Label("Quantity: ");
        quantityLabel.setStyle("-fx-font-weight: bold;");
        quantityField.setStyle(fieldStyle);

        newCategoryCheckBox.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        newCategoryField.setStyle("-fx-border-radius: 5px;");

        newCategoryCheckBox.setOnAction(e -> {
            if (newCategoryCheckBox.isSelected()) {
                categoryComboBox.setVisible(false);
                grid.add(newCategoryField, 1, 1);
            } else {
                newCategoryField.clear();
                grid.getChildren().remove(newCategoryField);
                categoryComboBox.setVisible(true);
            }
        });

        submitButton.setStyle("-fx-background-color: #0DB2AE; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 16px;");
        submitButton.setPrefWidth(150);
        submitButton.setPrefHeight(40);
        submitButton.setCursor(Cursor.HAND);
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #10C3B1; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 16px;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #0DB2AE; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 16px;"));

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(categoryLabel, 0, 1);
        grid.add(categoryComboBox, 1, 1);
        grid.add(newCategoryCheckBox, 2, 1);
        grid.add(supplierLabel, 0, 2);
        grid.add(supplierComboBox, 1, 2);
        grid.add(purchaseDateLabel, 0, 3);
        grid.add(purchaseDatePicker, 1, 3);
        grid.add(purchasePriceLabel, 4, 0);
        grid.add(purchasePriceField, 5, 0);
        grid.add(sellingPriceLabel, 4, 1);
        grid.add(sellingPriceField, 5, 1);
        grid.add(quantityLabel, 4, 2);
        grid.add(quantityField, 5, 2);

        HBox gridBox = new HBox(grid);
        gridBox.setAlignment(Pos.CENTER);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(submitButton);

        formBox.getChildren().addAll(gridBox, buttonBox);

        return formBox;
    }

    public static class ItemData {
    	private final Item item;
        private final StringProperty name;
        private final StringProperty category;
        private final StringProperty supplierName;
        private final StringProperty purchaseDate;
        private final DoubleProperty purchasePrice;
        private final DoubleProperty sellingPrice;
        private final IntegerProperty quantity;
        
        public ItemData(Item item, int quantity) {
        	this.item = item;
            this.name = new SimpleStringProperty(item.getName());
            this.category = new SimpleStringProperty(item.getCategory());
            this.supplierName = new SimpleStringProperty(item.getSupplier().getSupplierName());
            this.purchaseDate = new SimpleStringProperty(item.getPurchaseDate().toString());
            this.purchasePrice = new SimpleDoubleProperty(item.getPurchasePrice());
            this.sellingPrice = new SimpleDoubleProperty(item.getSellingPrice());
            this.quantity = new SimpleIntegerProperty(quantity);
        }

		public StringProperty nameProperty() { return name; }
        public String getName() { return name.get(); }
        
        public StringProperty categoryProperty() { return category; }
        public String getCategory() { return category.get();}
        
        public StringProperty supplierNameProperty() { return supplierName; }
        public String getSupplierName() { return supplierName.get();}
        
        public StringProperty purchaseDateProperty() { return purchaseDate; }
        public String getPurchaseDate() { return purchaseDate.get(); }
        
        public DoubleProperty purchasePriceProperty() { return purchasePrice; }
        public double getPurchasePrice() { return purchasePrice.get(); }
        
        public DoubleProperty sellingPriceProperty() { return sellingPrice; }
        public double getSellingPrice() { return sellingPrice.get(); }
        
        public IntegerProperty quantityProperty() { return quantity; }
        public int getQuantity() { return quantity.get(); }
        
        public Item getItem() { return this.item; }
    }
    
    private TextField searchField = new TextField();
    private FlowPane flowPane = new FlowPane();
	private ArrayList<Employee> cashiers = new ArrayList<>();
    private boolean[] isExpanded;
	private ScrollPane pane = new ScrollPane();
    private ArrayList<Button> empButtons = new ArrayList<>();
    
    public TextField getSearchField() { return this.searchField; }
    public FlowPane getFlowPane() { return this.flowPane; }
    public ArrayList<Employee> getCashiers() { return this.cashiers; }
    public boolean[] getIsExpanded() { return this.isExpanded; }
    public ScrollPane getPane() { return this.pane; }
    public ArrayList<Button> getEmpButtons() { return this.empButtons; }
    
    public void displayCashierPerformanceView(){
    	if(!((Manager)employee).getPermShowCashierPerformance()) {
    		showAlert(AlertType.ERROR, "Permission Error", "You don't have permission to view cashier performance!");
    		return;
    	} else if((Administrator.getEmployees().size() == 0 || Administrator.getEmployees() == null)) {
    		showAlert(AlertType.INFORMATION, "Cashiers", "There are no cashiers to view!");
    		return;
    	}
    	this.setRight(null);
    	pane.setContent(null);
    	pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    	pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    	
    	VBox viewEmployeesContent = new VBox();
    	viewEmployeesContent.setAlignment(Pos.CENTER);
    	viewEmployeesContent.setPadding(new Insets(20));
    	viewEmployeesContent.setStyle("-fx-background-color: #F9F9F9;");
        
        flowPane.setHgap(5);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10));
        flowPane.setStyle("-fx-background-color: #f9f9f9;");
        
        searchField.setPromptText("Search...");
        searchField.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-padding: 5px;");
        searchField.setPrefWidth(200);
        searchField.setMinWidth(200);
        searchField.setMaxWidth(200);
        
        HBox searchBarContainer = new HBox(searchField);
        searchBarContainer.setSpacing(10);
        searchBarContainer.setPadding(new Insets(10));
        searchBarContainer.setAlignment(Pos.TOP_CENTER);
        searchBarContainer.setStyle("-fx-border-radius: 10px;");
        
        cashiers.clear();
        for (Employee emp: Administrator.getEmployees()) {
        	if(emp instanceof Cashier && ((Manager)employee).getSectorList().contains(((Cashier)emp).getSector())) {
	        	cashiers.add(emp);
        	}
        }
        isExpanded = new boolean[cashiers.size()];
        
        for(int i = 0; i < isExpanded.length; i++) {
            isExpanded[i] = false;
        }
                             
        VBox root = new VBox(searchBarContainer, flowPane);
        root.getChildren().addAll();
        this.setCenter(root);
    }
    
    public Button createCashierCard(Cashier cashier, ArrayList<Button> cashierButtons, ScrollPane pane, ArrayList<Employee> cashiers, boolean[] isExpanded) {
    	VBox cashierCard = new VBox(10);
    	cashierCard.setAlignment(Pos.CENTER);
        String cashierCardStyle = "-fx-background-color: #ffffff; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
        String cashierCardHoverStyle = "-fx-background-color: #f2f0f0; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
        cashierCard.setStyle(cashierCardStyle);
        cashierCard.setPrefWidth(175);
        cashierCard.setMaxWidth(175);

        Text cashierText = new Text(cashier.getEmployeeName() + " (Cashier)");
        cashierText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        cashierText.setFill(Color.BLACK);

        ImageView imageView = new ImageView(new Image("images/user.png"));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        cashierCard.getChildren().addAll(imageView, cashierText);
                
        Button cashierButton = new Button();
        cashierButton.setGraphic(cashierCard);
        cashierButton.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;");
        cashierButton.setCursor(Cursor.HAND);
        cashierButtons.add(cashierButton);
        cashierButton.setOnAction(e -> {
        	pane.setContent(null);
            int cashierIndex = cashiers.indexOf(cashier);
            for (int j = 0; j < cashierButtons.size(); j++) {
            	cashierButtons.get(j).getGraphic().setStyle(cashierCardStyle);
            }
            
            if (!isExpanded[cashierIndex]) {
                for (int j = 0; j < isExpanded.length; j++) {
                    isExpanded[j] = false;
                }
                this.setRight(pane);
                cashierButton.getGraphic().setStyle(cashierCardHoverStyle);
                ScrollPane cashierBox = displayCashierStatistics(cashier);
                pane.setContent(cashierBox);
                isExpanded[cashierIndex] = true;
            } else {
                this.setRight(null);
                isExpanded[cashierIndex] = false;
            }
        });
        
       return cashierButton;
    }

    public ScrollPane displayCashierStatistics(Cashier cashier) {
    	
    	Label startLabel = new Label("Start Date:");
    	DatePicker startDatePicker = new DatePicker();
    	startDatePicker.setPromptText("Select start date");
    	startDatePicker.setStyle("-fx-pref-width: 140px;");

    	Label endLabel = new Label("End Date:");
    	DatePicker endDatePicker = new DatePicker();
    	endDatePicker.setPromptText("Select end date (optional)");
    	endDatePicker.setStyle("-fx-pref-width: 180px;");

    	Button submitDatesButton = new Button("Submit");
    	submitDatesButton.setCursor(Cursor.HAND);

    	HBox dateBox = new HBox(10, startLabel, startDatePicker, endLabel, endDatePicker, submitDatesButton);
    	dateBox.setPadding(new Insets(10));
    	dateBox.setAlignment(Pos.CENTER);

    	dateBox.setStyle("-fx-background-color: #f0f0f0; " + 
    	                 "-fx-border-color: #c0c0c0; " +
    	                 "-fx-border-radius: 10; " + 
    	                 "-fx-padding: 15; " +
    	                 "-fx-background-radius: 10;");

    	startLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	endLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	submitDatesButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold;");

    	StackPane centerPane = new StackPane();
    	centerPane.setPadding(new Insets(20));
    	centerPane.getChildren().add(dateBox);
    	StackPane.setAlignment(dateBox, Pos.CENTER);

    	CategoryAxis xAxis_showNumberOfBills = new CategoryAxis();
    	NumberAxis yAxis_showNumberOfBills = new NumberAxis();
    	xAxis_showNumberOfBills.setLabel("Date");
    	yAxis_showNumberOfBills.setLabel("Quantity");

    	BarChart<String, Number> barChart_showNumberOfBills = new BarChart<>(xAxis_showNumberOfBills, yAxis_showNumberOfBills);
    	barChart_showNumberOfBills.setLegendVisible(false);
    	XYChart.Series<String, Number> series_showNumberOfBills = new XYChart.Series<>();

    	CategoryAxis xAxis_showNumberOfItemsSold = new CategoryAxis();
    	NumberAxis yAxis_showNumberOfItemsSold = new NumberAxis();
    	xAxis_showNumberOfItemsSold.setLabel("Date");
    	yAxis_showNumberOfItemsSold.setLabel("Quantity");

    	BarChart<String, Number> barChart_showNumberOfItemsSold = new BarChart<>(xAxis_showNumberOfItemsSold, yAxis_showNumberOfItemsSold);
    	barChart_showNumberOfItemsSold.setLegendVisible(false);
    	barChart_showNumberOfItemsSold.setTitle("Items Sold");
    	XYChart.Series<String, Number> series_showNumberOfItemsSold = new XYChart.Series<>();
    	
    	// Bar Chart Total Revenue
    	CategoryAxis xAxis_showTotalRevenue = new CategoryAxis();
    	NumberAxis yAxis_showTotalRevenue = new NumberAxis();
    	xAxis_showTotalRevenue.setLabel("Date");
    	yAxis_showTotalRevenue.setLabel("Revenue (in $)");

    	BarChart<String, Number> barChart_showTotalRevenue = new BarChart<>(xAxis_showTotalRevenue, yAxis_showTotalRevenue);
    	barChart_showTotalRevenue.setLegendVisible(false);
    	barChart_showTotalRevenue.setTitle("Items Sold");
    	XYChart.Series<String, Number> series_showTotalRevenue = new XYChart.Series<>();

    	VBox chartPane = new VBox(20);
    	chartPane.setPadding(new Insets(10, 30, 10, 30));

    	submitDatesButton.setOnAction(e -> {
    	    LocalDate startDate = startDatePicker.getValue();
    	    LocalDate endDate = endDatePicker.getValue();

    	    if (startDate == null) {
    	        showAlert(AlertType.ERROR, "Date Selection", "Please select at least the start date");
    	    } else if ((endDate == null)){
    	    	try {
    	    		series_showNumberOfBills.getData().clear();
    	    		series_showNumberOfItemsSold.getData().clear();
    	    		series_showTotalRevenue.getData().clear();
    	    		barChart_showNumberOfBills.getData().clear();
    	    		barChart_showNumberOfItemsSold.getData().clear();
    	    		barChart_showTotalRevenue.getData().clear();
	    	        chartPane.getChildren().clear();
	
	    	        String startDateString = startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	    	        
	    	        int showNumberOfBills = ((Manager)employee).showNumberOfBills(cashier.getEmployeeID(), Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    	        int showNumberOfItemsSold = ((Manager)employee).showNumberOfItemsSold(cashier.getEmployeeID(), Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    	        double showTotalRevenue = ((Manager)employee).showTotalRevenue(cashier.getEmployeeID(), Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	
	    	        barChart_showNumberOfBills.setTitle("Number of Bills: " + showNumberOfBills);
	    	        series_showNumberOfBills.getData().add(new XYChart.Data<>(startDateString, showNumberOfBills));
	    	        barChart_showNumberOfBills.getData().add(series_showNumberOfBills);
	    	        series_showNumberOfBills.getData().get(0).getNode().setStyle("-fx-bar-fill: #ed0909;");
	
	    	        barChart_showNumberOfItemsSold.setTitle("Number of Items Sold: " + showNumberOfItemsSold);
	    	        series_showNumberOfItemsSold.getData().add(new XYChart.Data<>(startDateString, showNumberOfItemsSold));
	    	        barChart_showNumberOfItemsSold.getData().add(series_showNumberOfItemsSold);
	    	        series_showNumberOfItemsSold.getData().get(0).getNode().setStyle("-fx-bar-fill: #4a1d8c;");
	    	        
	    	        barChart_showTotalRevenue.setTitle("Total Revenue: $" + showTotalRevenue);
	    	        series_showTotalRevenue.getData().add(new XYChart.Data<>(startDateString, showTotalRevenue));
	    	        barChart_showTotalRevenue.getData().add(series_showTotalRevenue);
	    	        series_showTotalRevenue.getData().get(0).getNode().setStyle("-fx-bar-fill: #048517;");
	
	    	        chartPane.getChildren().addAll(barChart_showNumberOfBills, barChart_showNumberOfItemsSold, barChart_showTotalRevenue);
    	    	} catch(Exception ex) {
    	    		showAlert(AlertType.ERROR, "Error", ex.getMessage());
    	    	}
    	    } else {
    	    	try {
    	    		series_showNumberOfBills.getData().clear();
    	    		series_showNumberOfItemsSold.getData().clear();
    	    		series_showTotalRevenue.getData().clear();
    	    		barChart_showNumberOfBills.getData().clear();
    	    		barChart_showNumberOfItemsSold.getData().clear();
    	    		barChart_showTotalRevenue.getData().clear();
	    	        chartPane.getChildren().clear();
	
	    	        String startDateString = startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	    	        String endDateString = endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	    	        
	    	        int showNumberOfBills = ((Manager)employee).showNumberOfBills(cashier.getEmployeeID(), Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    	        int showNumberOfItemsSold = ((Manager)employee).showNumberOfItemsSold(cashier.getEmployeeID(), Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    	        double showTotalRevenue = ((Manager)employee).showTotalRevenue(cashier.getEmployeeID(), Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	
	    	        barChart_showNumberOfBills.setTitle("Number of Bills: " + showNumberOfBills);
	    	        series_showNumberOfBills.getData().add(new XYChart.Data<>(startDateString + " - " + endDateString, showNumberOfBills));
	    	        barChart_showNumberOfBills.getData().add(series_showNumberOfBills);
	    	        series_showNumberOfBills.getData().get(0).getNode().setStyle("-fx-bar-fill: #ed0909;");
	
	    	        barChart_showNumberOfItemsSold.setTitle("Number of Items Sold: " + showNumberOfItemsSold);
	    	        series_showNumberOfItemsSold.getData().add(new XYChart.Data<>(startDateString + " - " + endDateString, showNumberOfItemsSold));
	    	        barChart_showNumberOfItemsSold.getData().add(series_showNumberOfItemsSold);
	    	        series_showNumberOfItemsSold.getData().get(0).getNode().setStyle("-fx-bar-fill: #4a1d8c;");
	    	        
	    	        barChart_showTotalRevenue.setTitle("Total Revenue: $" + showTotalRevenue);
	    	        series_showTotalRevenue.getData().add(new XYChart.Data<>(startDateString + " - " + endDateString, showTotalRevenue));
	    	        barChart_showTotalRevenue.getData().add(series_showTotalRevenue);
	    	        series_showTotalRevenue.getData().get(0).getNode().setStyle("-fx-bar-fill: #048517;");
	
	    	        chartPane.getChildren().addAll(barChart_showNumberOfBills, barChart_showNumberOfItemsSold, barChart_showTotalRevenue);
    	    	} catch(Exception ex) {
    	    		showAlert(AlertType.ERROR, "Error", ex.getMessage());
    	    	}
    	    }
    	});

    	VBox box = new VBox(10);
    	box.getChildren().addAll(centerPane, chartPane);
    	box.setPadding(new Insets(10));

    	return new ScrollPane(box);
    }
    
    public void displayProfileView() {
    	this.setRight(null);

        HBox dashboardContent = new HBox(40);
        dashboardContent.setPadding(new Insets(30));
        dashboardContent.setAlignment(Pos.CENTER_LEFT);

        ImageView profileImageView = new ImageView(new Image("images/user.png"));
        profileImageView.setFitWidth(300);
        profileImageView.setFitHeight(300);
        profileImageView.setPreserveRatio(true);
        profileImageView.setStyle("-fx-border-color: #ccc; -fx-border-width: 3; -fx-padding: 10;");

        VBox profilePictureBox = new VBox(profileImageView);
        profilePictureBox.setAlignment(Pos.TOP_CENTER);

        VBox detailsBox = new VBox(20);
        detailsBox.setPadding(new Insets(20));

        String managerSector = null;
    	ArrayList<Sector> sectors = ((Manager)employee).getSectorList();
    	int i;
    	managerSector = sectors.get(0)+"";
    	for(i=1; i<sectors.size(); i++) {
    		managerSector+=", " + sectors.get(i);
    	}
        
        String[][] userDetails = {
            { "Employee ID:", employee.getEmployeeID() },
            { "Employee Name:", employee.getEmployeeName() },
            { "Employee Surname:", employee.getEmployeeSurname() },
            { "Date of Birth:", employee.getDoB()+"" },
            { "Phone Number:", employee.getPhoneNr() },
            { "Email:", employee.getEmail() },
            { "Address:", employee.getAddress() },
            { "Role:", "Manager"},
            { "Sector:", managerSector},
            { "Salary:", "$"+employee.getSalary()+""},
            { "Username:", employee.getUsername()},
            { "Password:", "*".repeat(employee.getPassword().length())}
        };

        for (String[] detail : userDetails) {
            HBox field = new HBox(15);
            Label label = new Label(detail[0]);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            Label value = new Label(detail[1]);
            value.setStyle("-fx-text-fill: #555; -fx-font-size: 15px;"); 

            field.getChildren().addAll(label, value);
            detailsBox.getChildren().add(field);
        }

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 16px;");
        changePasswordButton.setCursor(Cursor.HAND);
        changePasswordButton.setOnAction(e -> showChangePasswordWindow());

        detailsBox.getChildren().add(changePasswordButton);

        dashboardContent.getChildren().addAll(profilePictureBox, detailsBox);
        this.setCenter(dashboardContent);
    }
    
    private Stage popupStage = new Stage();
    private PasswordField currentPasswordField = new PasswordField();
    private PasswordField newPasswordField = new PasswordField();
    private PasswordField confirmNewPasswordField = new PasswordField();
    private Button changePasswordSubmitButton = new Button("Submit");

    public TextField getCurrentPasswordField() { return this.currentPasswordField; }
	public TextField getNewPasswordField() { return this.newPasswordField; }
    public TextField getConfirmNewPasswordField() { return this.confirmNewPasswordField; }
    public Stage getPopupStage() { return this.popupStage; }
    public Button getChangePasswordSubmitButton() { return this.changePasswordSubmitButton; }

    private void showChangePasswordWindow() {
        popupStage.setTitle("Change Password");
        popupStage.setResizable(false);

        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(15, 15, 5, 15));
        popupContent.setAlignment(Pos.TOP_LEFT);

        currentPasswordField.setPromptText("Enter Current Password");
        newPasswordField.setPromptText("Enter New Password");
        confirmNewPasswordField.setPromptText("Confirm New Password");

        changePasswordSubmitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        changePasswordSubmitButton.setCursor(Cursor.HAND);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        cancelButton.setCursor(Cursor.HAND);

        HBox buttonBox = new HBox(10, changePasswordSubmitButton, cancelButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        cancelButton.setOnAction(e -> popupStage.close());

        popupContent.getChildren().addAll(new Label("Current Password:"), currentPasswordField, new Label("New Password:"), newPasswordField, new Label("Confirm Password:"), confirmNewPasswordField, buttonBox);

        Platform.runLater(() -> popupContent.requestFocus());
        
        Scene popupScene = new Scene(popupContent, 300, 245);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
    private void configureButton(Button button, String style, String hoverStyle) {
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(style + hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        button.setOnAction(e -> button.setStyle(style + hoverStyle));
        button.setCursor(Cursor.HAND);
    }
    
    public void navigateToLogin() {
        LoginController loginController = new LoginController(stage);
        Scene loginScene = new Scene(loginController.getView(), 350, 430);
        stage.setScene(loginScene);
        stage.setTitle("Login - Marvis Electronics");
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
 	    stage.setX((screenBounds.getWidth() - 350) / 2);
 	    stage.setY((screenBounds.getHeight() - 430) / 2);
        
        stage.show();
    }
    
    public void updateTable() {
    	if(tableView != null) {
    		tableView.getItems().clear();
    	}
        for(ItemData t: tableItems) {
        	tableView.getItems().add(t); 
        }
        tableView.refresh(); 
    }
    
    private Button submitOptionButton = new Button("Submit");
    private ComboBox<String> optionComboBox = new ComboBox<>();
    private HBox chartPane = new HBox(20);

    public Button getSubmitOptionButton() { return this.submitOptionButton; }
    public ComboBox<String> getOptionComboBox() { return this.optionComboBox; }
    public HBox getChartPane() { return this.chartPane; }
    
    public void displayStatisticsView() {
        if (!((Manager) employee).getPermShowStatisticsAboutItems()) {
            showAlert(AlertType.ERROR, "Permission Error", "You don't have permission to view statistics about items!");
            return;
        }

        this.setRight(null);

        Label optionLabel = new Label("Choose an option:");
        optionComboBox.getItems().clear();
        optionComboBox.getItems().addAll("Daily", "Monthly", "Annually");
        submitOptionButton.setCursor(Cursor.HAND);
        HBox optionBox = new HBox(10, optionLabel, optionComboBox, submitOptionButton);
        optionBox.setPadding(new Insets(10));
        optionBox.setAlignment(Pos.CENTER);

        optionBox.setStyle("-fx-background-color: #f0f0f0; " + 
                           "-fx-border-color: #c0c0c0; " +
                           "-fx-border-radius: 10; " + 
                           "-fx-padding: 15; " +
                           "-fx-background-radius: 10;");
        optionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        submitOptionButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold;");

        StackPane centerPane = new StackPane();
        centerPane.setPadding(new Insets(20));
        centerPane.getChildren().add(optionBox);
        StackPane.setAlignment(optionBox, Pos.CENTER);

        chartPane.getChildren().clear();
        chartPane.setPadding(new Insets(10, 30, 10, 30));

        VBox box = new VBox(10);
        box.getChildren().addAll(centerPane, chartPane);
        box.setPadding(new Insets(10));

        this.setCenter(box);
    }

    public void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();    
    }
}
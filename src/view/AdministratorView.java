package view;

import java.time.LocalDate;
import java.util.ArrayList;

import controller.LoginController;
import javafx.application.Platform;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
import model.Administrator;
import model.Cashier;
import model.Employee;
import model.Manager;
import model.Sector;
import model.Supplier;

public class AdministratorView extends BorderPane{

	private static final Button employeeButton = new Button("Employees");
    private static final Button viewTotalCostButton = new Button("View Total Cost");
    private static final Button viewTotalIncomeButton = new Button("View Total Income");
    private static final Button supplierButton = new Button("Suppliers");
    private static final Button profileButton = new Button("Profile");
    private static final Button logoutButton = new Button("Logout");
    
    public Button getEmployeesButton() { return employeeButton; }
    public Button getViewTotalCostButton() { return viewTotalCostButton; }
    public Button getTotalIncomeButton() { return viewTotalIncomeButton; }
    public Button getSupplierButton() { return supplierButton; }
    public Button getProfileButton() { return profileButton; }
    public Button getLogoutButton() { return logoutButton; }
    
    private final Stage stage;
    private final Employee employee;
    
    public Employee getEmployee() { return this.employee; }
    
    public AdministratorView(Stage stage, Employee employee) {
        this.stage = stage;
        this.employee = employee;
		this.isExpandedEmp = null;
		this.isExpanded = null;
		displaySupplierView();
        setupView();
    }

    public void setupView() {
    	this.getChildren().clear();
        BorderPane sidebar = createSidebar();
        this.setLeft(sidebar);
        displayEmployeeView();
    }

    public BorderPane createSidebar() {
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
    	
    	configureButton(employeeButton, buttonStyle, buttonHoverStyle);
    	configureButton(viewTotalCostButton, buttonStyle, buttonHoverStyle);
    	configureButton(viewTotalIncomeButton, buttonStyle, buttonHoverStyle);
    	configureButton(supplierButton, buttonStyle, buttonHoverStyle);
    	configureButton(profileButton, profileStyle, profileHoverStyle);
    	configureButton(logoutButton, logoutStyle, logoutHoverStyle);

    	ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(employeeButton);
        buttons.add(viewTotalCostButton);
        buttons.add(viewTotalIncomeButton);
        buttons.add(supplierButton);
    	
    	ImageView logoutIcon = new ImageView(new Image("images/logout.png"));
    	logoutIcon.setFitWidth(16);
    	logoutIcon.setFitHeight(16);
    	logoutIcon.setPreserveRatio(true);
    	logoutButton.setGraphic(logoutIcon);
    	
    	VBox buttonContainer = new VBox(employeeButton, viewTotalCostButton, viewTotalIncomeButton, supplierButton);
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
    
    public void inicializeSuppliers() {
    	suppliers.clear();
    	 for (Employee emp: Administrator.getEmployees()) {
         	if(emp instanceof Manager) {
         		for(Supplier s: ((Manager)emp).getSupplierList()) {
         			 suppliers.add(s);
         		}
         	}
         }
    }
    
    private TextField searchField = new TextField();
    private FlowPane flowPane = new FlowPane();
	private ArrayList<Supplier> suppliers = new ArrayList<>();
    private boolean[] isExpanded;
	private ScrollPane pane = new ScrollPane();
    private ArrayList<Button> supplierButtons = new ArrayList<>();
    
    public TextField getSearchField() { return this.searchField; }
    public FlowPane getFlowPane() { return this.flowPane; }
    public ArrayList<Supplier> getSuppliers() { return this.suppliers; }
    public boolean[] getIsExpanded() { return this.isExpanded; }
    public ScrollPane getPane() { return this.pane; }
    public ArrayList<Button> getSupplierButtons() { return this.supplierButtons; }
    
    public void displaySupplierView(){
    	this.setRight(null);
    	
    	pane.setContent(null);
    	pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    	pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    	
    	VBox viewSupplierContent = new VBox();
    	viewSupplierContent.setAlignment(Pos.CENTER);
    	viewSupplierContent.setPadding(new Insets(20));
    	viewSupplierContent.setStyle("-fx-background-color: #F9F9F9;");
        
        flowPane.setHgap(5);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10));
        flowPane.setStyle("-fx-background-color: #f9f9f9;");
        
        suppliers.clear();
   	 	for(Employee emp: Administrator.getEmployees()) {
        	if(emp instanceof Manager) {
        		for(Supplier s: ((Manager)emp).getSupplierList()) {
        			 suppliers.add(s);
        		}
        	}
        }
        
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
        
        isExpanded = new boolean[100];
        
        for(int i = 0; i < isExpanded.length; i++) {
            isExpanded[i] = false;
        }
        
        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 10px 18px; -fx-border-radius: 50%; -fx-background-radius: 50%;");
        addButton.setCursor(Cursor.HAND);
        addButton.setOnAction(e -> displayNewSupplierForm());
        
        StackPane root = new StackPane();
        root.getChildren().addAll(new VBox(searchBarContainer, flowPane), addButton);

        StackPane.setAlignment(addButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(addButton, new Insets(20));

        this.setCenter(root);
    }
    
    public Button createSupplierCard(Supplier supplier, ArrayList<Button> supplierButtons, ScrollPane pane, ArrayList<Supplier> suppliers, boolean[] isExpanded) {
    	VBox supplierCard = new VBox(10);
    	supplierCard.setAlignment(Pos.CENTER);
        String supplierCardStyle = "-fx-background-color: #ffffff; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
        String supplierCardHoverStyle = "-fx-background-color: #f2f0f0; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
        supplierCard.setStyle(supplierCardStyle);
        supplierCard.setPrefWidth(175);
        supplierCard.setMaxWidth(175);
        
        Text supplierText = new Text(supplier.getSupplierName() + " (Supplier)");
        supplierText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        supplierText.setFill(Color.BLACK);

        ImageView imageView = new ImageView(new Image("images/user.png"));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        supplierCard.getChildren().addAll(imageView, supplierText);
                
        Button supplierButton = new Button();
        supplierButton.setGraphic(supplierCard);
        supplierButton.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;");
        supplierButton.setCursor(Cursor.HAND);
        supplierButtons.add(supplierButton);
        supplierButton.setOnAction(e -> {
        	pane.setContent(null);
            int supplierIndex = suppliers.indexOf(supplier);
            
            for (int j = 0; j < supplierButtons.size(); j++) {
            	supplierButtons.get(j).getGraphic().setStyle(supplierCardStyle);
            }
            
            if (!isExpanded[supplierIndex]) {
                for (int j = 0; j < isExpanded.length; j++) {
                    isExpanded[j] = false;
                }
                
                this.setRight(pane);
                supplierButton.getGraphic().setStyle(supplierCardHoverStyle);
                ScrollPane supplierBox = displaySupplier(supplier);
                pane.setContent(supplierBox);
                isExpanded[supplierIndex] = true;
            } else {
                this.setRight(null);
                isExpanded[supplierIndex] = false;
            }
        });
       return supplierButton;
    }
        
    private TextField supplierNameField = new TextField();
    private TextField supplierPhoneNrField = new TextField();
    private TextField supplierAddressField = new TextField();
    private TextField itemsOfferedField = new TextField();
    private ComboBox<String> managerComboBox = new ComboBox<>();
    private Button supplierSubmitButton = new Button("Submit");
	private Stage primaryStage = new Stage();

    public TextField getSupplierNameField() { return this.supplierNameField; }
    public TextField getSupplierPhoneNrField() { return this.supplierPhoneNrField; }
    public TextField getSupplierAddressField() { return this.supplierAddressField; }
    public TextField getItemsOfferedField() { return this.itemsOfferedField; }
    public ComboBox<String> getManagerComboBox() { return this.managerComboBox; }
    public Button getSupplierSubmitButton() { return this.supplierSubmitButton; }
    public Stage getPrimaryStage() { return this.primaryStage; }
    
    private void displayNewSupplierForm() {
    	GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        
        supplierNameField.setText("");
        supplierPhoneNrField.setText("");
        supplierAddressField.setText("");
        itemsOfferedField.setText("");

        Label supplierName = new Label("Name:");
        Label supplierPhoneNr = new Label("Phone Number:");
        Label supplierAddress = new Label("Address:");
        Label itemsOffered = new Label("Items:");
        Label manager = new Label("Manager:");

        grid.add(supplierName, 0, 0);
        grid.add(supplierNameField, 1, 0);
        grid.add(supplierPhoneNr, 0, 1);
        grid.add(supplierPhoneNrField, 1, 1);
        grid.add(supplierAddress, 0, 2);
        grid.add(supplierAddressField, 1, 2);
        grid.add(itemsOffered, 0, 3);
        grid.add(itemsOfferedField, 1, 3);
        grid.add(manager, 0, 4);
        grid.add(managerComboBox, 1, 4);

        String submitStyle = "-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 10px 14px;";
        supplierSubmitButton.setStyle(submitStyle);
        supplierSubmitButton.setCursor(Cursor.HAND);
        supplierSubmitButton.setOnMouseEntered(e -> supplierSubmitButton.setStyle(submitStyle + "-fx-background-color: #026dc2"));
        supplierSubmitButton.setOnMouseExited(e -> supplierSubmitButton.setStyle(submitStyle));

        VBox box = new VBox();
        box.getChildren().addAll(grid, supplierSubmitButton);
        box.setAlignment(Pos.CENTER);
        
        Platform.runLater(() -> grid.requestFocus());
        
        Scene popupScene = new Scene(box, 285, 300);
        primaryStage.setTitle("New Supplier");
        primaryStage.setScene(popupScene);
        primaryStage.setResizable(false);
        primaryStage.showAndWait();
    }
    
    private VBox detailsBox = new VBox(20);
    private Button saveSupplierButton = new Button("Save");
    private Supplier selectedSupplier = null;
    private Button removeSupplierButton = new Button("Remove");
    
    public VBox getDetailsBox() { return this.detailsBox; }
    public Button getSaveSupplierButton() { return this.saveSupplierButton; }
    public Supplier getSelectedSupplier() { return selectedSupplier; }
    public Button getRemoveSupplierButton() { return this.removeSupplierButton; }

    private ScrollPane displaySupplier(Supplier supplier) {
        VBox dashboardContent = new VBox(40);
        dashboardContent.setPadding(new Insets(30));
        dashboardContent.setAlignment(Pos.CENTER_LEFT); 

        ImageView profileImageView = new ImageView(new Image("images/user.png"));
        profileImageView.setFitWidth(250);
        profileImageView.setFitHeight(250); 
        profileImageView.setPreserveRatio(true);
        profileImageView.setStyle("-fx-border-color: #ccc; -fx-border-width: 3; -fx-padding: 10;");

        VBox profilePictureBox = new VBox(profileImageView);
        profilePictureBox.setAlignment(Pos.TOP_CENTER); 

        detailsBox.setPadding(new Insets(20));
        
        selectedSupplier = supplier;
        
        ArrayList<String> itemsOffered = supplier.getItemsOffered();
    	int i;
    	String itemsAsString = itemsOffered.get(0)+"";
    	for(i=1; i<itemsOffered.size(); i++) {
    		itemsAsString+=", " + itemsOffered.get(i);
    	}
        
        String[][] userDetails = {
            { "Supplier ID:", supplier.getSupplierID() },
            { "Supplier Name:", supplier.getSupplierName() },
            { "Phone Number:", supplier.getSupplierPhoneNr() },
            { "Address:", supplier.getSupplierAddress() },
            { "Items:", itemsAsString},
        };
        
        detailsBox.getChildren().clear();
        for (String[] detail : userDetails) {
            HBox field = new HBox(15);
            Label label = new Label(detail[0]);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            TextField value = new TextField(detail[1]);
            value.setStyle("-fx-text-fill: #555; -fx-font-size: 15px;");
            value.setDisable(true);
            if(label.getText().equals("Phone Number:") || label.getText().equals("Address:") || label.getText().equals("Items:")){
            	value.setDisable(false);
            	
            }
            field.getChildren().addAll(label, value);
            detailsBox.getChildren().add(field);
        }
        
        String saveButtonStyle = "-fx-background-color: #07b851; -fx-text-fill: white;-fx-font-size: 15px; -fx-padding: 10px 14px;";
        saveSupplierButton.setStyle(saveButtonStyle);
        saveSupplierButton.setCursor(Cursor.HAND);
        saveSupplierButton.setOnMouseEntered(e -> saveSupplierButton.setStyle(saveButtonStyle + "-fx-background-color: #08a349"));
        saveSupplierButton.setOnMouseExited(e -> saveSupplierButton.setStyle(saveButtonStyle));
        
        String removeButtonStyle = "-fx-background-color: #A80000; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 10px 14px;";
        removeSupplierButton.setStyle(removeButtonStyle);
        removeSupplierButton.setCursor(Cursor.HAND);
        removeSupplierButton.setOnMouseEntered(e -> removeSupplierButton.setStyle(removeButtonStyle + "-fx-background-color: #960303"));
        removeSupplierButton.setOnMouseExited(e -> removeSupplierButton.setStyle(removeButtonStyle));

        HBox buttons = new HBox(10, saveSupplierButton, removeSupplierButton);
        buttons.setAlignment(Pos.CENTER);
        detailsBox.getChildren().add(buttons);

        dashboardContent.getChildren().addAll(profilePictureBox, detailsBox);
        
        ScrollPane scrollPane = new ScrollPane(dashboardContent);
        return scrollPane;
    }
    
    private boolean[] isExpandedEmp;
    private ArrayList<Employee> employees = Administrator.getEmployees();
	private ScrollPane empPane = new ScrollPane();
    private ArrayList<Button> empButtons = new ArrayList<>();
    private FlowPane empFlowPane = new FlowPane();
    private TextField searchEmployeeField = new TextField();

    public boolean[] getIsExpandedEmp() { return isExpandedEmp; }
    public ArrayList<Employee> getEmployees() { return employees; } 
    public ArrayList<Button> getEmpButtons() { return this.empButtons; }
    public ScrollPane getEmpPane() { return this.empPane; }
    public FlowPane getEmpFlowPane() { return this.empFlowPane; }
    public TextField getSearchEmployeeField() { return this.searchEmployeeField; }

    public void displayEmployeeView(){
    	if((Administrator.getEmployees().size() == 0 || Administrator.getEmployees() == null)) {
    		showAlert(AlertType.INFORMATION, "Employees", "There are no employees to view!");
    		return;
    	}
    	
    	this.setRight(null);
    	
    	empPane.setContent(null);
    	empPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    	empPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    	
    	VBox viewEmployeesContent = new VBox();
    	viewEmployeesContent.setAlignment(Pos.CENTER);
    	viewEmployeesContent.setPadding(new Insets(20));
    	viewEmployeesContent.setStyle("-fx-background-color: #F9F9F9;");
        
    	empFlowPane.setHgap(5);
    	empFlowPane.setVgap(10);
    	empFlowPane.setPadding(new Insets(10));
    	empFlowPane.setStyle("-fx-background-color: #f9f9f9;");
        
        searchEmployeeField.setPromptText("Search...");
        searchEmployeeField.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-padding: 5px;");
        searchEmployeeField.setPrefWidth(200);
        searchEmployeeField.setMinWidth(200);
        searchEmployeeField.setMaxWidth(200);
        
        HBox searchBarContainer = new HBox(searchEmployeeField);
        searchBarContainer.setSpacing(10);
        searchBarContainer.setPadding(new Insets(10));
        searchBarContainer.setAlignment(Pos.TOP_CENTER);
        searchBarContainer.setStyle("-fx-border-radius: 10px;");
        
        isExpandedEmp = new boolean[100];
        
        for(int i = 0; i < isExpandedEmp.length; i++) {
        	isExpandedEmp[i] = false;
        }
        
        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 10px 18px; -fx-border-radius: 50%; -fx-background-radius: 50%;");
        addButton.setCursor(Cursor.HAND);
        addButton.setOnAction(e -> displayNewEmployeeForm());
        
        StackPane root = new StackPane();
        root.getChildren().addAll(new VBox(searchBarContainer, empFlowPane), addButton);

        StackPane.setAlignment(addButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(addButton, new Insets(20));

        this.setCenter(root);
    }
    
    public Button createEmployeeCard(Employee emp, ArrayList<Button> empButtons, ScrollPane pane, ArrayList<Employee> employees, boolean[] isExpanded) {
    	VBox empCard = new VBox(10);
    	empCard.setAlignment(Pos.CENTER);
        String empCardStyle = "-fx-background-color: #ffffff; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
        String empCardHoverStyle = "-fx-background-color: #f2f0f0; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
        empCard.setStyle(empCardStyle);
        empCard.setPrefWidth(175);
        empCard.setMaxWidth(175);

        Text empText = new Text(emp.getEmployeeName() + " (" + emp.getRole() +")");
        empText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        empText.setFill(Color.BLACK);

        ImageView imageView = new ImageView(new Image("images/user.png"));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        empCard.getChildren().addAll(imageView, empText);
        
        Button empButton = new Button();
        empButton.setGraphic(empCard);
        empButton.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;");
        empButton.setCursor(Cursor.HAND);
        empButtons.add(empButton);
        empButton.setOnAction(e -> {
        	pane.setContent(null);
            int empIndex = employees.indexOf(emp);
            for (int j = 0; j < empButtons.size(); j++) {
                empButtons.get(j).getGraphic().setStyle(empCardStyle);
            }
            
            if (!isExpanded[empIndex]) {
                for (int j = 0; j < isExpanded.length; j++) {
                    isExpanded[j] = false;
                }
                
                this.setRight(pane);
                empButton.getGraphic().setStyle(empCardHoverStyle);
                ScrollPane empBox = displayEmployee(emp);
                pane.setContent(empBox);
                isExpanded[empIndex] = true;
            } else {
                this.setRight(null);
                isExpanded[empIndex] = false;
            }
        });
       return empButton;
    }
        
    private Button submitEmpButton = new Button("Submit");
    private TextField empNameField = new TextField();
    private TextField empSurnameField = new TextField();
    private DatePicker DoBField = new DatePicker();
    private TextField phoneNrField = new TextField();
    private TextField emailField = new TextField();
    private TextField salaryField = new TextField();
    private TextField addressField = new TextField();
    private RadioButton cashierButton = new RadioButton("Cashier");
    private RadioButton managerButton = new RadioButton("Manager");
    private RadioButton homeAppliancesRadio = new RadioButton(Sector.HomeAppliances.toString());
    private RadioButton mobileDevicesRadio = new RadioButton(Sector.MobileDevices.toString());
    private RadioButton computersRadio = new RadioButton(Sector.Computers.toString());
    private RadioButton smallElectronicsRadio = new RadioButton(Sector.SmallElectronics.toString());
    private RadioButton gamingRadio = new RadioButton(Sector.Gaming.toString());
    private RadioButton accessoriesRadio = new RadioButton(Sector.Accessories.toString());
    private RadioButton cameraRadio = new RadioButton(Sector.Camera.toString());
    private Stage primaryEmpStage = new Stage();

    public Button getSubmitEmpButton() { return submitEmpButton; }
    public TextField getEmpNameField() { return empNameField; }
    public TextField getEmpSurnameField() { return empSurnameField; }
    public DatePicker getDoBField() { return DoBField; }
    public TextField getPhoneNrField() { return phoneNrField; }
    public TextField getEmailField() { return emailField; }
    public TextField getSalaryField() { return salaryField; }
    public TextField getAddressField() { return addressField; }
    public RadioButton getCashierButton() { return cashierButton; }
    public RadioButton getManagerButton() { return managerButton; }
    public RadioButton getHomeAppliancesRadioButton() { return homeAppliancesRadio; }
    public RadioButton getMobileDevicesRadioButton() { return mobileDevicesRadio; }
    public RadioButton getComputersRadioButton() { return computersRadio; }
    public RadioButton getSmallElectronicsRadioButton() { return smallElectronicsRadio; }
    public RadioButton getGamingRadioButton() { return gamingRadio; }
    public RadioButton getAccessoriesRadioButton() { return accessoriesRadio; }
    public RadioButton getCameraRadioButton() { return cameraRadio; }
    public Stage getPrimaryEmpStage() { return primaryEmpStage; }
    
    private void displayNewEmployeeForm() {
    	empNameField.setText("");
        empSurnameField.setText("");
        phoneNrField.setText("");
        emailField.setText("");
        salaryField.setText("");
        addressField.setText("");
        cashierButton.setSelected(false);
        managerButton.setSelected(false);
        homeAppliancesRadio.setSelected(false);
        mobileDevicesRadio.setSelected(false);
        computersRadio.setSelected(false);
        smallElectronicsRadio.setSelected(false);
        gamingRadio.setSelected(false);
        accessoriesRadio.setSelected(false);
        cameraRadio.setSelected(false);
        
    	GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        Label empName = new Label("Name:");
        Label empSurname = new Label("Surname:");
        Label DoB = new Label("DoB:");
        DoBField.setValue(LocalDate.now());
        Label phoneNr = new Label("Phone Number:");
        Label email = new Label("Email:");
        Label salary = new Label("Salary:");
        Label address = new Label("Address:");

        Label role = new Label("Role:");
        ToggleGroup roleGroup = new ToggleGroup();
        cashierButton.setToggleGroup(roleGroup);
        managerButton.setToggleGroup(roleGroup);

        Label sectorLabel = new Label("Sector:");

        ToggleGroup sectorGroup = new ToggleGroup();
        homeAppliancesRadio.setToggleGroup(sectorGroup);
        mobileDevicesRadio.setToggleGroup(sectorGroup);
        computersRadio.setToggleGroup(sectorGroup);
        smallElectronicsRadio.setToggleGroup(sectorGroup);
        gamingRadio.setToggleGroup(sectorGroup);
        accessoriesRadio.setToggleGroup(sectorGroup);
        cameraRadio.setToggleGroup(sectorGroup);

        managerButton.setOnAction(e -> {
            if (managerButton.isSelected()) {
            	homeAppliancesRadio.setToggleGroup(null);
                mobileDevicesRadio.setToggleGroup(null);
                computersRadio.setToggleGroup(null);
                smallElectronicsRadio.setToggleGroup(null);
                gamingRadio.setToggleGroup(null);
                accessoriesRadio.setToggleGroup(null);
                cameraRadio.setToggleGroup(null);
                homeAppliancesRadio.setSelected(false);
                mobileDevicesRadio.setSelected(false);
                computersRadio.setSelected(false);
                smallElectronicsRadio.setSelected(false);
                gamingRadio.setSelected(false);
                accessoriesRadio.setSelected(false);
                cameraRadio.setSelected(false);
            }
        });

        cashierButton.setOnAction(e -> {
            if (cashierButton.isSelected()) {
                homeAppliancesRadio.setSelected(false);
                mobileDevicesRadio.setSelected(false);
                computersRadio.setSelected(false);
                smallElectronicsRadio.setSelected(false);
                gamingRadio.setSelected(false);
                accessoriesRadio.setSelected(false);
                cameraRadio.setSelected(false);
                homeAppliancesRadio.setToggleGroup(sectorGroup);
                mobileDevicesRadio.setToggleGroup(sectorGroup);
                computersRadio.setToggleGroup(sectorGroup);
                smallElectronicsRadio.setToggleGroup(sectorGroup);
                gamingRadio.setToggleGroup(sectorGroup);
                accessoriesRadio.setToggleGroup(sectorGroup);
                cameraRadio.setToggleGroup(sectorGroup);
            }
        });

        grid.add(empName, 0, 0);
        grid.add(empNameField, 1, 0);
        grid.add(empSurname, 0, 1);
        grid.add(empSurnameField, 1, 1);
        grid.add(DoB, 0, 2);
        grid.add(DoBField, 1, 2);
        grid.add(phoneNr, 0, 3);
        grid.add(phoneNrField, 1, 3);
        grid.add(email, 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(salary, 0, 5);
        grid.add(salaryField, 1, 5);
        grid.add(address, 0, 6);
        grid.add(addressField, 1, 6);
        grid.add(role, 0, 7);
        grid.add(cashierButton, 1, 7);
        grid.add(managerButton, 1, 8);
        grid.add(sectorLabel, 0, 9);
        grid.add(homeAppliancesRadio, 1, 9);
        grid.add(mobileDevicesRadio, 1, 10);
        grid.add(computersRadio, 1, 11);
        grid.add(smallElectronicsRadio, 1, 12);
        grid.add(gamingRadio, 1, 13);
        grid.add(accessoriesRadio, 1, 14);
        grid.add(cameraRadio, 1, 15);

        String submitStyle = "-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 10px 14px;";
        submitEmpButton.setStyle(submitStyle);
        submitEmpButton.setCursor(Cursor.HAND);
        submitEmpButton.setOnMouseEntered(e -> submitEmpButton.setStyle(submitStyle + "-fx-background-color: #026dc2"));
        submitEmpButton.setOnMouseExited(e -> submitEmpButton.setStyle(submitStyle));

        VBox box = new VBox();
        box.getChildren().addAll(grid, submitEmpButton);
        box.setAlignment(Pos.CENTER);
        
        Platform.runLater(() -> grid.requestFocus());
        
        Scene popupScene = new Scene(box, 315, 600);
        primaryEmpStage.setTitle("New Employee Form");
        primaryEmpStage.setScene(popupScene);
        primaryEmpStage.setResizable(false);
        primaryEmpStage.show();
    }
    
    private VBox employeeDetailsBox = new VBox(20);
    private Employee selectedEmp = null;
    private Button saveButton = new Button("Save");
    private CheckBox createBillCheckbox = new CheckBox();
    private CheckBox showTodaysBillsCheckbox = new CheckBox();
    private CheckBox totalOfAllBillsCheckbox = new CheckBox();
    private CheckBox showCashierPerformanceCheckbox = new CheckBox();
    private CheckBox addItemInventoryCheckbox = new CheckBox();
    private CheckBox createCategoryCheckbox = new CheckBox();
    private CheckBox showStatisticsAboutItemsCheckbox = new CheckBox();
    private Button removeButton = new Button("Remove");

    
    public VBox getEmployeeDetailsBox() { return employeeDetailsBox; }
    public Employee getSelectedEmp() { return this.selectedEmp; }
    public Button getSaveButton() { return this.saveButton; }
    public CheckBox getCreateBillCheckbox() { return this.createBillCheckbox; }
    public CheckBox getShowTodaysBillsCheckbox() { return this.showTodaysBillsCheckbox; }
    public CheckBox getTotalOfAllBillsCheckbox() { return this.totalOfAllBillsCheckbox; }
    public CheckBox getShowCashierPerformanceCheckbox() { return this.showCashierPerformanceCheckbox; }
    public CheckBox getAddItemInventoryCheckbox() { return this.addItemInventoryCheckbox; }
    public CheckBox getCreateCategoryCheckbox() { return this.createCategoryCheckbox; }
    public CheckBox showStatisticsAboutItemsCheckbox() { return this.showStatisticsAboutItemsCheckbox; }
    public Button getRemoveButton() { return removeButton; }
    
    private ScrollPane displayEmployee(Employee emp) {
        VBox dashboardContent = new VBox(40); 
        dashboardContent.setPadding(new Insets(30));
        dashboardContent.setAlignment(Pos.CENTER_LEFT);
        
        selectedEmp = emp;
        
        ImageView profileImageView = new ImageView(new Image("images/user.png"));
        profileImageView.setFitWidth(250);
        profileImageView.setFitHeight(250);
        profileImageView.setPreserveRatio(true);
        profileImageView.setStyle("-fx-border-color: #ccc; -fx-border-width: 3; -fx-padding: 10;");

        VBox profilePictureBox = new VBox(profileImageView);
        profilePictureBox.setAlignment(Pos.TOP_CENTER); 

        employeeDetailsBox.getChildren().clear();
        employeeDetailsBox.setPadding(new Insets(20));
        
        String empSector = null;
        if(emp instanceof Cashier) {
        	empSector = ((Cashier)emp).getSector().toString();
        }
        else if(emp instanceof Manager) {
        	ArrayList<Sector> sectors = ((Manager)emp).getSectorList();
        	int i;
        	empSector = sectors.get(0)+"";
        	for(i=1; i<sectors.size(); i++) {
        		empSector+=", " + sectors.get(i);
        	}
        }
        
        String[][] userDetails = {
            { "Employee ID:", emp.getEmployeeID() },
            { "Employee Name:", emp.getEmployeeName() },
            { "Employee Surame:", emp.getEmployeeSurname() },
            { "Date of Birth:", emp.getDoB()+"" },
            { "Phone Number:", emp.getPhoneNr() },
            { "Email:", emp.getEmail() },
            { "Address:", emp.getAddress() },
            { "Role:", emp.getRole()},
            { "Salary:", "$"+emp.getSalary()+""},
            { "Sector:", empSector},
            { "Username:", emp.getUsername()},
            { "Password:", "*".repeat(emp.getPassword().length())}
        };
        
        for (String[] detail : userDetails) {
            HBox field = new HBox(15);
            Label label = new Label(detail[0]);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;"); 
            TextField value = new TextField(detail[1]);
            value.setStyle("-fx-text-fill: #555; -fx-font-size: 15px;"); 
            value.setDisable(true);
            if(label.getText().equals("Phone Number:") || label.getText().equals("Email:") || label.getText().equals("Address:") 
            		|| label.getText().equals("Salary:") || label.getText().equals("Sector:")){
            	value.setDisable(false);
            	
            }
            field.getChildren().addAll(label, value);
            employeeDetailsBox.getChildren().add(field);
        }
        
        Label createBillLabel = new Label("Create Bill:");
    	Label showTodaysBillsLabel = new Label("View Today's Bills:");
    	Label totalOfAllBillsLabel = new Label("Total of all Bills:");
        Label showCashierPerformanceLabel = new Label("View Cashier Performance:");
    	Label addItemInventoryLabel = new Label("Add Item to Inventory:");
    	Label createCategoryLabel = new Label("Create Category:");
        Label showStatisticsAboutItemsLabel = new Label("Show Statistics about Items:");
    	
        if(emp instanceof Cashier) {
        	createBillCheckbox.setSelected(((Cashier)emp).getPermCreateBill());
        	HBox createBillField = new HBox(15, createBillLabel, createBillCheckbox);
        	createBillLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        	employeeDetailsBox.getChildren().add(createBillField);
        	
        	showTodaysBillsCheckbox.setSelected(((Cashier)emp).getPermShowTodayBills());
        	HBox showTodaysBillsField = new HBox(15, showTodaysBillsLabel, showTodaysBillsCheckbox);
        	showTodaysBillsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        	employeeDetailsBox.getChildren().add(showTodaysBillsField);
        	
            totalOfAllBillsCheckbox.setSelected(((Cashier)emp).getPermTotalOfAllBills());
            HBox totalOfAllBillsField = new HBox(15, totalOfAllBillsLabel, totalOfAllBillsCheckbox);
        	totalOfAllBillsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        	employeeDetailsBox.getChildren().add(totalOfAllBillsField);
        }
        
        if(emp instanceof Manager) {
        	showCashierPerformanceCheckbox.setSelected(((Manager)emp).getPermShowCashierPerformance());
        	HBox showCashierPerformanceField = new HBox(15, showCashierPerformanceLabel, showCashierPerformanceCheckbox);
        	showCashierPerformanceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        	employeeDetailsBox.getChildren().add(showCashierPerformanceField);
        	
        	addItemInventoryCheckbox.setSelected(((Manager)emp).getPermAddItemInventory());
        	HBox addItemInventoryField = new HBox(15, addItemInventoryLabel, addItemInventoryCheckbox);
        	addItemInventoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        	employeeDetailsBox.getChildren().add(addItemInventoryField);
        	
        	createCategoryCheckbox.setSelected(((Manager)emp).getPermCreateCategory());
            HBox createCategoryField = new HBox(15, createCategoryLabel, createCategoryCheckbox);
            createCategoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            employeeDetailsBox.getChildren().add(createCategoryField);
        	
        	showStatisticsAboutItemsCheckbox.setSelected(((Manager)emp).getPermShowStatisticsAboutItems());
            HBox showStatisticsAboutItemsField = new HBox(15, showStatisticsAboutItemsLabel, showStatisticsAboutItemsCheckbox);
            showStatisticsAboutItemsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            employeeDetailsBox.getChildren().add(showStatisticsAboutItemsField);
        }

        Button changePasswordButton = new Button("Change Password");
        String changePasswordStyle = "-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 10px 14px;";
        changePasswordButton.setStyle(changePasswordStyle); 
        changePasswordButton.setCursor(Cursor.HAND);
        changePasswordButton.setOnMouseEntered(e -> changePasswordButton.setStyle(changePasswordStyle + "-fx-background-color: #026dc2"));
        changePasswordButton.setOnMouseExited(e -> changePasswordButton.setStyle(changePasswordStyle));
        changePasswordButton.setOnAction(e -> showChangePasswordUserWindow());
        
        String saveButtonStyle = "-fx-background-color: #07b851; -fx-text-fill: white;-fx-font-size: 15px; -fx-padding: 10px 14px;";
        saveButton.setStyle(saveButtonStyle);
        saveButton.setCursor(Cursor.HAND);
        saveButton.setOnMouseEntered(e -> saveButton.setStyle(saveButtonStyle + "-fx-background-color: #08a349"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle(saveButtonStyle));
        
        String removeButtonStyle = "-fx-background-color: #A80000; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 10px 14px;";
        removeButton.setStyle(removeButtonStyle); 
        removeButton.setCursor(Cursor.HAND);
        removeButton.setOnMouseEntered(e -> removeButton.setStyle(removeButtonStyle + "-fx-background-color: #960303"));
        removeButton.setOnMouseExited(e -> removeButton.setStyle(removeButtonStyle));

        HBox buttons = new HBox(10, changePasswordButton, saveButton, removeButton);
        employeeDetailsBox.getChildren().add(buttons);

        dashboardContent.getChildren().addAll(profilePictureBox, employeeDetailsBox);
        ScrollPane scrollPane = new ScrollPane(dashboardContent);
        return scrollPane;
    }
    
    private Button changePasswordButton = new Button("Submit");
    private PasswordField newUserPasswordField = new PasswordField();
    private PasswordField confirmNewUserPasswordField = new PasswordField();
    private Stage popupUserPasswordStage = new Stage();
    
    public Button getChangePasswordButton() { return this.changePasswordButton; }
    public PasswordField getNewUserPasswordField() { return this.newUserPasswordField; }
    public PasswordField getConfirmUserNewPasswordField() { return this.confirmNewUserPasswordField; }
    public Stage getPopupUserPasswordStage() { return this.popupUserPasswordStage; }
    
    public void showChangePasswordUserWindow() {
    	popupUserPasswordStage.setTitle("Change Password");
    	popupUserPasswordStage.setResizable(false);

        VBox popupContent = new VBox(10); 
        popupContent.setPadding(new Insets(15, 15, 5, 15));
        popupContent.setAlignment(Pos.TOP_LEFT);

        newUserPasswordField.setPromptText("Enter New Password");
        confirmNewUserPasswordField.setPromptText("Confirm New Password");

        changePasswordButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        changePasswordButton.setCursor(Cursor.HAND);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setOnAction(e -> popupUserPasswordStage.close());
        
        HBox buttonBox = new HBox(10, changePasswordButton, cancelButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        cancelButton.setOnAction(e -> popupPasswordStage.close());
        popupContent.getChildren().addAll(new Label("New Password:"), newUserPasswordField, new Label("Confirm Password:"), confirmNewUserPasswordField, buttonBox);

        Platform.runLater(() -> popupContent.requestFocus());
        
        Scene popupScene = new Scene(popupContent, 300, 190);
        popupUserPasswordStage.setScene(popupScene);
        popupUserPasswordStage.showAndWait();
    }
    
    public void displayViewTotalCostView() {
    	this.setRight(null);        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Date");
        yAxis.setLabel("Cost (in $)");

        double total = ((Administrator)employee).getTotalCost();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Lifetime Expenses: $" + total);
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cost");
        series.getData().add(new XYChart.Data<>("Total", total));

        barChart.getData().add(series);
        series.getData().get(0).getNode().setStyle("-fx-bar-fill: #ed0909;");

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(barChart);
        stackPane.setPadding(new Insets(10, 30, 10, 30));
        this.setCenter(stackPane);
    }
    
    public void displayViewTotalIncomeView() {
    	this.setCenter(null);
    	this.setRight(null);        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Date");
        yAxis.setLabel("Income (in $)");

        double total = ((Administrator)employee).getTotalIncome();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Lifetime Income: $" + total);
        barChart.setLegendVisible(false);
        barChart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Income");
        
        series.getData().add(new XYChart.Data<>("Total", total));

        barChart.getData().add(series);
        series.getData().get(0).getNode().setStyle("-fx-bar-fill: #ed0909;");
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(barChart);
        stackPane.setPadding(new Insets(10, 30, 10, 30));
        
        this.setCenter(stackPane);
    }
    
    private VBox profileDetailsBox = new VBox(20); 
    private Button saveProfileButton = new Button("Save");

    public VBox getProfileDetailsBox() { return this.profileDetailsBox; }
    public Button getSaveProfileButton() { return this.saveProfileButton; }
    
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

        profileDetailsBox.setPadding(new Insets(20));
        profileDetailsBox.getChildren().clear();
        String[][] userDetails = {
            { "Employee ID:", employee.getEmployeeID() },
            { "Employee Name:", employee.getEmployeeName() },
            { "Employee Surname:", employee.getEmployeeSurname() },
            { "Date of Birth:", employee.getDoB()+"" },
            { "Phone Number:", employee.getPhoneNr() },
            { "Email:", employee.getEmail() },
            { "Address:", employee.getAddress() },
            { "Role:", "Administrator"},
            { "Salary:", "$"+employee.getSalary()+""},
            { "Username:", employee.getUsername()},
            { "Password:", "*".repeat(employee.getPassword().length())}
        };
        
        for (String[] detail : userDetails) {
            HBox field = new HBox(15);
            Label label = new Label(detail[0]);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
            TextField value = new TextField(detail[1]);
            value.setStyle("-fx-text-fill: #555; -fx-font-size: 13px;"); 
            value.setDisable(true);
            if(label.getText().equals("Phone Number:") || label.getText().equals("Email:") || label.getText().equals("Address:") || label.getText().equals("Salary:")) {
            	value.setDisable(false);
            	
            }
            field.getChildren().addAll(label, value);
            profileDetailsBox.getChildren().add(field);
        }
        
        Button changePasswordButton = new Button("Change Password");
        String changePasswordStyle = "-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 16px;";
        changePasswordButton.setStyle(changePasswordStyle);
        changePasswordButton.setCursor(Cursor.HAND);
        changePasswordButton.setOnMouseEntered(e -> changePasswordButton.setStyle(changePasswordStyle + "-fx-background-color: #026dc2"));
        changePasswordButton.setOnMouseExited(e -> changePasswordButton.setStyle(changePasswordStyle));
        changePasswordButton.setOnAction(e -> showChangePasswordWindow());
        
        String saveButtonStyle = "-fx-background-color: #07b851; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 16px;";
        saveProfileButton.setStyle(saveButtonStyle);
        saveProfileButton.setCursor(Cursor.HAND);
        saveProfileButton.setOnMouseEntered(e -> saveProfileButton.setStyle(saveButtonStyle + "-fx-background-color: #08a349"));
        saveProfileButton.setOnMouseExited(e -> saveProfileButton.setStyle(saveButtonStyle));

        HBox buttons = new HBox(10, changePasswordButton, saveProfileButton);
        profileDetailsBox.getChildren().add(buttons);

        dashboardContent.getChildren().addAll(profilePictureBox, profileDetailsBox);
        this.setCenter(dashboardContent);
    }

    private Button changePasswordSubmitButton = new Button("Submit");
    private PasswordField newPasswordField = new PasswordField();
    private PasswordField confirmNewPasswordField = new PasswordField();
    private Stage popupPasswordStage = new Stage();
    
    public Button getChangePasswordSubmitButton() { return this.changePasswordSubmitButton; }
    public PasswordField getNewPasswordField() { return this.newPasswordField; }
    public PasswordField getConfirmNewPasswordField() { return this.confirmNewPasswordField; }
    public Stage getPopupPasswordStage() { return this.popupPasswordStage; }
    
    public void showChangePasswordWindow() {
    	popupPasswordStage.setTitle("Change Password");
    	popupPasswordStage.setResizable(false);

        VBox popupContent = new VBox(10); 
        popupContent.setPadding(new Insets(15, 15, 5, 15));
        popupContent.setAlignment(Pos.TOP_LEFT);

        newPasswordField.setPromptText("Enter New Password");
        confirmNewPasswordField.setPromptText("Confirm New Password");

        changePasswordSubmitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        changePasswordSubmitButton.setCursor(Cursor.HAND);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setOnAction(e -> popupPasswordStage.close());
        
        HBox buttonBox = new HBox(10, changePasswordSubmitButton, cancelButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        cancelButton.setOnAction(e -> popupPasswordStage.close());
        popupContent.getChildren().addAll(new Label("New Password:"), newPasswordField, new Label("Confirm Password:"), confirmNewPasswordField, buttonBox);

        Platform.runLater(() -> popupContent.requestFocus());
        
        Scene popupScene = new Scene(popupContent, 300, 190);
        popupPasswordStage.setScene(popupScene);
        popupPasswordStage.showAndWait();
    }
    
    public void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();    
    }
    
    public void configureButton(Button button, String style, String hoverStyle) {
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
}
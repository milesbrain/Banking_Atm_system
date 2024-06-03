package atm.project1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Atm_controllr implements Initializable {
    Stage stage;
    Scene scene;

    @FXML
    private TextField customerId;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField first_nameTextField;
    @FXML
    private TextField homeAddressTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField OccupationTextField;
    @FXML
    private PasswordField passwordField;

    private Connection getConnection() throws SQLException, IOException {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("Sorry, unable to find db.properties");
            }
            prop.load(input);
        }

        String url = prop.getProperty("db.url");
        String username = prop.getProperty("db.username");
        String password = prop.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void saveToDatabase(ActionEvent event) {
        Alert alert;
        String accountNo = customerId.getText();
        String email = homeAddressTextField.getText();
        String firstName = first_nameTextField.getText();
        String address = homeAddressTextField.getText();
        String passwords = passwordField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String surname = surnameTextField.getText();
        String occupation = OccupationTextField.getText();

        if (accountNo.isEmpty() || email.isEmpty() ||
                firstName.isEmpty() || address.isEmpty() ||
                passwords.isEmpty() || phoneNumber.isEmpty() ||
                surname.isEmpty() || occupation.isEmpty()) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("One or More field is empty");
            alert.showAndWait();
            return;
        }

        try (Connection connection = getConnection()) {
            String customerexistQuery = "SELECT * FROM registration WHERE CustomerID = ?";
            try (PreparedStatement customerExistStatement = connection.prepareStatement(customerexistQuery)) {
                customerExistStatement.setString(1, accountNo);
                ResultSet customerExistResult = customerExistStatement.executeQuery();

                if (customerExistResult.next()) {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Alert");
                    alert.setContentText("CustomerID already exists!");
                    alert.showAndWait();
                    return;
                }
            }

            String phoneNumberExistQuery = "SELECT * FROM registration WHERE phone_Number = ?";
            try (PreparedStatement phoneNumberExistStatement = connection.prepareStatement(phoneNumberExistQuery)) {
                phoneNumberExistStatement.setString(1, phoneNumber);
                ResultSet phoneNumberExistResult = phoneNumberExistStatement.executeQuery();

                if (phoneNumberExistResult.next()) {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Alert");
                    alert.setContentText("PhoneNumber already exists!");
                    alert.showAndWait();
                    return;
                }
            }

            String insertQuery = "INSERT INTO registration (CustomerID, Email, First_Name, Home_Address, password, phone_Number, Surname, Occupation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, accountNo);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, address);
                preparedStatement.setString(5, passwords);
                preparedStatement.setString(6, phoneNumber);
                preparedStatement.setString(7, surname);
                preparedStatement.setString(8, occupation);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("success");
                    alert.setContentText("Your Account has been created");
                    alert.showAndWait();
                    connection.close();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardInformation.fxml"));
                        Parent roots = loader.load();

                        CardInformation cardInformationController = loader.getController();
                        cardInformationController.displayName(accountNo);

                        stage = (Stage) customerId.getScene().getWindow();
                        scene = new Scene(roots);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Data not saved to the database successfully");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void CreateNewAccountButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/CreateNewAccount.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        saveToDatabase(event);
    }

    @FXML
    public void ChangePin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ChangePin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void BalanceButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Balacecheck.fxml"));
        Parent roots = loader.load();


        BalanceController controller = loader.getController();


        stage =  (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(roots);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void DepositButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DepositAccount.fxml"));
        Parent roots = loader.load();


        confirmcontroller controller = loader.getController();


        stage =  (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(roots);
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    public void withdrawalButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Withdrawal.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void TransferButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Transfer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        if (customerId != null) {
            customerId.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d{0,10}?")) {
                    customerId.setText(oldValue);
                } else if (newValue.length() < 10) {
                    showAlert("AccountNo must be 10 digits \nNOTE:Warning keeps showing until number is up to 10");
                }
            });

            emailTextField.focusedProperty().addListener((arg0, oldValues, newValues) -> {
                if (!newValues) {
                    if (!emailTextField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                        showAlert("Invalid Email Address");
                    }
                }
            });

            phoneNumberTextField.focusedProperty().addListener((arg, oldVal, newVal) -> {
                if (!newVal) {
                    if (!phoneNumberTextField.getText().matches("\\d{11}")) {
                        showAlert("Input a valid Phone Number");

                    }
                }
            });
        }
    }
}

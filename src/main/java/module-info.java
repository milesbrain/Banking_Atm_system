module atm.project1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens atm.project1 to javafx.fxml;
    exports atm.project1;
}
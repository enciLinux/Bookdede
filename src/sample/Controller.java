package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Controller {
    @FXML
    private Button aRegistro;

    public void onRegistroButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/registro.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Bookdede - Registro");
        stage.setScene(new Scene(root));
        hideDefaultScreen(stage, "hide");
    }

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Bookdede - Login");
        stage.setScene(new Scene(root));
        hideDefaultScreen(stage, "close");
    }

    private void hideDefaultScreen(Stage stage, String type) {
        Stage defaultScreen = (Stage) aRegistro.getScene().getWindow();
        if (type.equals("hide"))
            stage.setOnHidden((WindowEvent e) -> defaultScreen.show());

        defaultScreen.close();
        stage.show();
    }

}

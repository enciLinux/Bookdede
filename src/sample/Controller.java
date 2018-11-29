package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Controller {
    public Button aRegistro;
   // ...xd
    public void onRegistroButtonClick(ActionEvent actionEvent) throws IOException {
     
        Parent root = FXMLLoader.load(getClass().getResource("registro.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Registro");
        stage.setScene(new Scene(root));

        Stage registroStage = (Stage)aRegistro.getScene().getWindow();

        stage.setOnHidden((WindowEvent e) -> registroStage.show());

        stage.show();

        registroStage.hide();
    }

    public void onLoginButtonClick(ActionEvent actionEvent) {
    }
}

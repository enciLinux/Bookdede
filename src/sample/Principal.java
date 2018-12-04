package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Principal implements Initializable {
    @FXML
    private Button bEliminar;
    @FXML
    private Button bPerfil;
    @FXML
    private Button bAgregar;

    private String usuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuario = Usuario.getInstance();
        if (!usuario.equals("Mati") && !usuario.equals("Ara")) {
            bAgregar.setVisible(false);
            bEliminar.setVisible(false);
        }
    }

    public void aAgregarLibro(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/registro.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Bookdede - Login");
        stage.setScene(new Scene(root));
        hideDefaultScreen(stage, "close");
    }

    private void hideDefaultScreen(Stage stage, String type) {
        Stage defaultScreen = (Stage) bAgregar.getScene().getWindow();
        if (type.equals("hide"))
            stage.setOnHidden((WindowEvent e) -> defaultScreen.show());

        defaultScreen.close();
        stage.show();
    }

}

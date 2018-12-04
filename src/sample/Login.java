package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    @FXML
    private Button bLogin;
    @FXML
    private TextField jtfUsuario;
    @FXML
    private PasswordField jtfPass;

    public void iniciarSesion(ActionEvent actionEvent) throws SQLException, IOException {
        String usuario = jtfUsuario.getText();
        String pass = jtfPass.getText();
        boolean existe = comprobarUsuario(usuario, pass);
        if (existe) {
            String mensaje = "Bienvenido " + usuario + ".";
            alert("exito", mensaje);
            new Usuario(usuario);
            loginAPrincipal();
        } else {
            String mensaje = "Nombre y/o contraseña introducida incorrecta";
            alert("error", mensaje);
        }
    }

    private boolean comprobarUsuario(String usuario, String pass) throws SQLException {
        Statement st = MySQLConnect.getInstance().createStatement();
        String query = "SELECT nick,email FROM usuarios WHERE nick='" + usuario + "' " +
                "AND pass='" + pass + "';";
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    private void loginAPrincipal() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/principal.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Bookdede - Principal");
        stage.setScene(new Scene(root));
        closeLoginScreen(stage);
    }

    private void alert(String tipo, String mensaje) {
        Alert dialogoAlerta = null;
        if (tipo.equals("error")) {
            dialogoAlerta = new Alert(Alert.AlertType.ERROR);
            dialogoAlerta.setTitle("Error");
            dialogoAlerta.setHeaderText(null);
        }
        if (tipo.equals("exito")) {
            dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
            dialogoAlerta.setTitle("Error");
            dialogoAlerta.setHeaderText(null);
        }

        dialogoAlerta.setContentText(mensaje);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.showAndWait();
    }

    private void closeLoginScreen(Stage stage) {
        Stage defaultScreen = (Stage) bLogin.getScene().getWindow();
        stage.show();
        defaultScreen.close();
    }

}

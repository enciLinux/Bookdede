package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class editarUsuario {
    public Button jbModificarUsuario;
    @FXML
    private TextField jtfNombre;
    @FXML
    private PasswordField jtfPassConfirm;
    @FXML
    private PasswordField jtfPass;
    @FXML
    private TextField jtfNick;

    private void volverAPrincipal(String resultado) {
        Stage stage = (Stage) jbModificarUsuario.getScene().getWindow();
        stage.close();
    }

    public void modificarUsuario(ActionEvent actionEvent) throws SQLException {
        String nombre = jtfNombre.getText();
        String pass1 = jtfPass.getText();
        String pass2 = jtfPassConfirm.getText();
        String nick = jtfNick.getText();

        if (pass1.equals(pass2)) {
            PreparedStatement pstmt = null;
            String query = "UPDATE usuarios SET nombre=? WHERE nick=?;";
            pstmt = MySQLConnect.getInstance().prepareStatement(query);

            pstmt.setString(1, nombre);
            pstmt.setString(2, nick);

            int registrosAfectados = pstmt.executeUpdate();
            if(registrosAfectados>0){
                String mensaje = "El nombre se ha modificado correctamente.";
                alert("exito",mensaje);
                volverAPrincipal(mensaje);
            }

            pstmt.close();
        }
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
            dialogoAlerta.setTitle("Éxito");
            dialogoAlerta.setHeaderText(null);
        }

        dialogoAlerta.setContentText(mensaje);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.showAndWait();
    }

}

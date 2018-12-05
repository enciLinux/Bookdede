package sample;

import com.mysql.cj.protocol.Resultset;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Registro implements Initializable {

    @FXML
    private TextField jtfNick;
    @FXML
    private TextField jtfCorreo;
    @FXML
    private PasswordField jtfPass;
    @FXML
    private PasswordField jtfPassConfirm;
    @FXML
    private TextField jtfNombre;
    @FXML
    private TextField jtfCP;
    @FXML
    private TextField jtfTelefono;
    @FXML
    private ComboBox jcbProvincias;
    @FXML
    public Spinner jsANacimiento;
    @FXML
    private RadioButton jrbHombre;
    @FXML
    private RadioButton jrbMujer;
    @FXML
    private Button jbRegistrarUsuario;

    private ToggleGroup toggleGroup = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            agruparRadioButtons(toggleGroup, jrbHombre, jrbMujer);
            rellenarProvincias(jcbProvincias);
            setLimitesSpiner(jsANacimiento);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setLimitesSpiner(Spinner jsANacimiento) {
        jsANacimiento.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2000));
    }

    private void rellenarProvincias(ComboBox jcbProvincias) throws SQLException {
        Statement st = MySQLConnect.getInstance().createStatement();
        String query = "SELECT provincia FROM provincias;";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            jcbProvincias.getItems().add(rs.getString(1));
        }
    }

    private void agruparRadioButtons(ToggleGroup toggleGroup, RadioButton jrbHombre, RadioButton jrbMujer) {
        jrbHombre.setToggleGroup(toggleGroup);
        jrbMujer.setToggleGroup(toggleGroup);
    }

    public void registrarUsuario(ActionEvent actionEvent) throws SQLException {
        String nick = jtfNick.getText();
        String correo = jtfCorreo.getText();
        String pass = jtfPass.getText();
        String pass2 = jtfPassConfirm.getText();
        String nombre = jtfNombre.getText();
        String cp = jtfCP.getText();
        String genero = toggleGroup.getSelectedToggle().toString();
        if (genero.contains("Hombre"))
            genero = "Hombre";
        if (genero.contains("Mujer"))
            genero = "Mujer";

        String telefono = jtfTelefono.getText();
        String provincia = jcbProvincias.getSelectionModel().getSelectedItem().toString();
        String anyoNac = jsANacimiento.getValue().toString();

        if (pass.equals(pass2)) {
            boolean existe = comprobarUsuario(nick, correo);
            if (!existe)
                insertarUsuario(nick, nombre, correo, pass, provincia, cp, genero, anyoNac, telefono);
        }
    }

    private boolean comprobarUsuario(String nick, String correo) throws SQLException {
        Statement st = MySQLConnect.getInstance().createStatement();
        String query = "SELECT nick,email FROM usuarios WHERE nick='" + nick + "' OR email='" + correo + "';";
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            String error = "Este usuario ya existe.";
            alert("error", error);
            return true;
        }
        return false;
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

    private void insertarUsuario(String nick, String nombre, String correo, String pass, String provincia, String cp, String genero, String anyoNac, String telefono) throws SQLException {
        String query = "INSERT INTO usuarios (nick,nombre,email,pass,provincia,codPostal,genero,anyoNacimiento,telefono)"
                + "VALUES (?,?,?,?,?,?,?,?,?);";
        PreparedStatement pstmt = MySQLConnect.getInstance().prepareStatement(query);
        pstmt.setString(1, nick);
        pstmt.setString(2, nombre);
        pstmt.setString(3, correo);
        pstmt.setString(4, pass);
        pstmt.setString(5, provincia);
        pstmt.setString(6, cp);
        pstmt.setString(7, genero);
        pstmt.setInt(8, Integer.parseInt(anyoNac));
        pstmt.setString(9, telefono);

        int insert = pstmt.executeUpdate();
        if (insert > 0) {
            String resultado = "Registro de usuario completado.";
            alert("exito", resultado);
            volverAInicio(resultado);
        } else {
            System.out.println("Error al realizar la inserción.");
        }
    }

    private void volverAInicio(String resultado) {
        Stage stage = (Stage) jbRegistrarUsuario.getScene().getWindow();
        stage.close();
    }

}

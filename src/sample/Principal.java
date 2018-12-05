package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Principal implements Initializable {
    public Button bRefrescar;
    @FXML
    private MenuItem bCerrarSesion;
    @FXML
    private MenuItem bEditarPerfil;
    @FXML
    private TextField jtfNombre;
    @FXML
    private Slider jsValoracion;
    @FXML
    private DatePicker jdFecha;
    @FXML
    private GridPane gridLibros;
    @FXML
    private ComboBox jcbEditorial;
    @FXML
    private CheckBox jcDescargable;
    @FXML
    private ComboBox jcbGenero;
    @FXML
    private Button bPerfil;
    @FXML
    private Button bAgregar;

    private String usuario;

    public void refrescar(ActionEvent actionEvent) throws SQLException {
        getLibros("SELECT isbn, portada FROM libros;");

        if (!(jcbGenero.getItems().size() > 0)) {
            rellenarEditorial(jcbEditorial);
            rellenarGeneros(jcbGenero);
        }
    }

    private class MyEventHandler implements EventHandler<Event> {
        private String isbn;

        public MyEventHandler(String isbn) {
            this.isbn = isbn;
        }

        @Override
        public void handle(Event evt) {

            new Libro(isbn);
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("../fxml/libro.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Bookdede - Libro");
                stage.setScene(new Scene(root));
                hideDefaultScreen(stage, "hide");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bPerfil.setVisible(false);

        usuario = Usuario.getInstance();
        if (!usuario.equals("Mati") && !usuario.equals("Ara")) {
            bAgregar.setVisible(false);
            //bRefrescar.setVisible(false);
        }

        try {
            rellenarGeneros(jcbGenero);
            rellenarEditorial(jcbEditorial);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT isbn, portada FROM libros;";
        getLibros(query);

    }

    private void getLibros(String query) {
        Statement st = null;
        try {
            st = MySQLConnect.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            int contadorFila = 0;
            int contadorCol = 0;
            while (rs.next()) {
                String isbn = rs.getString("isbn");

                Label label = new Label();
                label.setMaxHeight(200);
                label.setMaxWidth(200);

                InputStream is = rs.getBinaryStream("portada");
                Image image1 = new Image(is);

                ImageView imageView = new ImageView(image1);
                imageView.setFitWidth(250);
                imageView.setFitHeight(350);
                //imageView.setPreserveRatio(true);
                label.setGraphic(imageView);
                label.setId(isbn);
                label.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler(isbn));


                gridLibros.addRow(contadorFila);
                gridLibros.add(label, contadorCol, contadorFila);
                contadorCol++;
                if (contadorCol == 2) {
                    contadorFila++;
                    contadorCol = 0;
                }
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void buscarPorTecleo(KeyEvent mouseEvent) {
        gridLibros.getChildren().clear();
        String nombre = jtfNombre.getText();
        String query = "SELECT isbn, portada FROM libros WHERE nombre LIKE '%" + nombre + "%';";
        Statement st = null;
        getLibros(query);
    }

    public void aEditarPerfil(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/editarUsuario.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Bookdede - Editar Usuario");
        stage.setScene(new Scene(root));
        hideDefaultScreen(stage, "hide", bPerfil);
    }

    private void hideDefaultScreen(Stage stage, String type, Button boton) {
        Stage defaultScreen = (Stage) boton.getScene().getWindow();
        if (type.equals("hide"))
            stage.setOnHidden((WindowEvent e) -> defaultScreen.show());

        defaultScreen.close();
        stage.show();
    }

    public void cerrarSesion(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Bookdede - Login");
        stage.setScene(new Scene(root));
        hideDefaultScreen(stage, "close");

    }

    public void aAgregarLibro(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/agregarLibro.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Bookdede - Agregar Libro");
        stage.setScene(new Scene(root));
        hideDefaultScreen(stage, "hide");
    }

    private void hideDefaultScreen(Stage stage, String type) {
        Stage defaultScreen = (Stage) bAgregar.getScene().getWindow();
        if (type.equals("hide"))
            stage.setOnHidden((WindowEvent e) -> defaultScreen.show());

        defaultScreen.close();
        stage.show();
    }

    private void rellenarGeneros(ComboBox jcbGenero) throws SQLException {
        Statement st = MySQLConnect.getInstance().createStatement();
        String query = "SELECT DISTINCT genero FROM libros;";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            jcbGenero.getItems().add(rs.getString(1));
        }
    }

    private void rellenarEditorial(ComboBox jcbEditorial) throws SQLException {
        Statement st = MySQLConnect.getInstance().createStatement();
        String query = "SELECT DISTINCT editorial FROM libros;";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            jcbEditorial.getItems().add(rs.getString(1));
        }
    }

    public void filtrarDescargable(ActionEvent actionEvent) throws IOException {
        gridLibros.getChildren().clear();
        String condicion = "";
        if (jcDescargable.isSelected()) {
            condicion = "IS NOT NULL";
        } else {
            condicion = "IS NULL";
        }
        String query = "SELECT isbn, portada FROM libros WHERE pdf " + condicion + ";";
        getLibros(query);
    }

    public void filtrarFecha(ActionEvent actionEvent) {
        gridLibros.getChildren().clear();
        String fecha = jdFecha.getValue().toString();
        fecha = getMySQLFormatoFecha(fecha);
        String query = "SELECT isbn, portada FROM libros WHERE fechaPublicacion > " + fecha + ";";
        getLibros(query);
    }

    private String getMySQLFormatoFecha(String fecha) {
        String dia = fecha.substring(fecha.lastIndexOf("-") + 1);
        String mes = fecha.substring(fecha.indexOf("-") + 1, fecha.lastIndexOf("-"));
        String anyo = fecha.substring(0, fecha.indexOf("-"));
        String fechaFormateada = anyo + mes + dia;
        return fechaFormateada;
    }

    public void filtrarPuntuacion(MouseEvent mouseDragEvent) {
        gridLibros.getChildren().clear();
        int valoracion = (int) jsValoracion.getValue();
        Statement st = null;
        String query = "SELECT isbn, portada FROM libros WHERE valoracion >= " + valoracion + ";";
        getLibros(query);
    }

    public void filtrarGenero(ActionEvent actionEvent) throws SQLException {
        gridLibros.getChildren().clear();
        String genero = jcbGenero.getSelectionModel().getSelectedItem().toString();
        String query = "SELECT isbn, portada FROM libros WHERE genero = '" + genero + "';";
        getLibros(query);
    }

    public void filtrarEditorial(ActionEvent actionEvent) throws SQLException {
        gridLibros.getChildren().clear();
        String editorial = jcbEditorial.getSelectionModel().getSelectedItem().toString();
        String query = "SELECT isbn, portada FROM libros WHERE editorial = '" + editorial + "';";
        getLibros(query);
    }
}

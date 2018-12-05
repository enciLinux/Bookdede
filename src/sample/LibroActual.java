package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class LibroActual implements Initializable {

    public Label jlSinopsis;
    public Label jlNombre;
    public Label jlAutor;
    public Label jlEditorial;
    public Label jlAnyo;
    public Label jlValoracion;
    public Label jlGenero;
    public Label jlPortada;
    public Button jbDescargarPDF;
    public Button bEliminarLibro;
    public Button bPendiente;
    public Button bFavoritos;
    public Button bLeido;

    private String isbn;
    private Blob pdf;

    public LibroActual() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bFavoritos.setVisible(false);
        this.bPendiente.setVisible(false);
        this.bLeido.setVisible(false);
        if(!Usuario.getInstance().equals("Ara") && !Usuario.getInstance().equals("Mati"))
            bEliminarLibro.setVisible(false);

        isbn = Libro.getInstance();
        String query = "SELECT * FROM libros WHERE isbn='" + isbn + "';";
        getLibro(query);

    }

    private void getLibro(String query) {

        Statement st = null;
        try {
            st = MySQLConnect.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            int contadorFila = 0;
            int contadorCol = 0;
            while (rs.next()) {
                //isbn	nombre Ascending 1	autor	editorial	genero	fechaPublicacion	valoracion	sinopsis	pdf	portada
                String isbn = rs.getString("isbn");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String genero = rs.getString("genero");
                String fecha = rs.getDate("fechaPublicacion").toString();
                int valoracion = rs.getInt("valoracion");
                String sinopsis = rs.getString("sinopsis");
                this.pdf = rs.getBlob("pdf");

                jlPortada.setMaxHeight(200);
                jlPortada.setMaxWidth(200);
                InputStream is = rs.getBinaryStream("portada");
                Image image1 = new Image(is);

                ImageView imageView = new ImageView(image1);
                imageView.setFitWidth(250);
                imageView.setFitHeight(350);
                //imageView.setPreserveRatio(true);
                jlPortada.setGraphic(imageView);
                //imageView.setPreserveRatio(true);

                mostrarDatos(isbn, nombre, autor, editorial, genero, fecha, valoracion, sinopsis);

            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDatos(String isbn, String nombre, String autor, String editorial, String genero, String fecha, int valoracion, String sinopsis) {
        jlValoracion.setText(String.valueOf(valoracion));
        jlNombre.setText(nombre);
        jlAutor.setText(autor);
        jlEditorial.setText(editorial);
        jlGenero.setText(genero);
        jlAnyo.setText(fecha);
        jlSinopsis.setText(sinopsis);
    }

    public void descargarPDF(ActionEvent actionEvent) {
        try {
            String nombre = jlNombre.getText();
            InputStream is = this.pdf.getBinaryStream();
            FileOutputStream fos = new FileOutputStream("../../Downloads/" + nombre + ".pdf");
            int b = 0;
            while ((b = is.read()) != -1) {
                fos.write(b);
            }
            String mensaje = "Se ha descargado el libro seleccionado:"+nombre;
            alert("exito",mensaje);


        } catch (SQLException | FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void volverAPrincipal(String resultado) {
        Stage stage = (Stage) bEliminarLibro.getScene().getWindow();
        stage.close();
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

    public void eliminarLibro(ActionEvent actionEvent) throws SQLException {
        Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoAlerta.setTitle("Bookdede - Confirmar");
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.setContentText("¿Está seguro que desea eliminarlo?");
        Optional<ButtonType> result = dialogoAlerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            PreparedStatement pstmt = null;
            String query = "DELETE FROM libros WHERE isbn=?;";
            pstmt = MySQLConnect.getInstance().prepareStatement(query);

            pstmt.setString(1, isbn);

            int registrosAfectados = pstmt.executeUpdate();
            if (registrosAfectados > 0) {
                String mensaje = "El libro se ha eliminado con éxito.";
                alert("exito", mensaje);
                volverAPrincipal(mensaje);
            }

            pstmt.close();
        }
    }

   /* public void agregarFavorito(ActionEvent actionEvent) throws SQLException {

        boolean existe = consultarFavorito(Usuario.getInstance(), isbn, "");
        if (!existe) {
            agregarFav();
        } else {
            existe = consultarFavorito(Usuario.getInstance(), isbn, "AND favoritos=1");
            if (existe) {
                quitarFav();
            } else {
                setFav();
            }
        }


    }*/

    private void setFav() throws SQLException {
        PreparedStatement pstmt = null;
        String query = "UPDATE listaslibros SET favoritos=1 WHERE nick=? AND isbn=?;";
        pstmt = MySQLConnect.getInstance().prepareStatement(query);

        pstmt.setString(1, Usuario.getInstance());
        pstmt.setString(2, isbn);

        pstmt.close();
    }

    private void quitarFav() throws SQLException {
        PreparedStatement pstmt = null;
        String query = "UPDATE listaslibros SET favoritos=0 WHERE nick=? AND isbn=?;";
        pstmt = MySQLConnect.getInstance().prepareStatement(query);

        pstmt.setString(1, Usuario.getInstance());
        pstmt.setString(2, isbn);

        pstmt.executeUpdate();

        pstmt.close();
    }

    private boolean consultarFavorito(String instance, String isbn, String condicion) {
        Statement st = null;
        boolean existe = false;

        try {

            st = MySQLConnect.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT favoritos FROM listaslibros WHERE nick='" + Usuario.getInstance() + "' " +
                    "AND isbn='" + isbn + "' " + condicion);
            if (rs != null) {
                existe = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }

    private void agregarFav() {
        String query = "INSERT INTO listaslibros (nick, isbn, favoritos)"
                + " VALUES (?,?,?);";
        try (PreparedStatement pstmt = MySQLConnect.getInstance().prepareStatement(query)) {
            pstmt.setString(1, Usuario.getInstance());
            pstmt.setString(2, isbn);
            pstmt.setBoolean(3, true);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarPendiente(ActionEvent actionEvent) {
    }

    public void agregarLeido(ActionEvent actionEvent) {
    }

    public void agregarFavorito(ActionEvent actionEvent) {
    }
}

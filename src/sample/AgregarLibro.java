package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AgregarLibro implements Initializable {

    @FXML
    private Spinner jsValoracion;
    @FXML
    private TextField jtfGenero;
    @FXML
    private TextField jtfISBN;
    @FXML
    private Label jlImg;
    @FXML
    private Label jlPDF;
    @FXML
    private TextField jtfNombre;
    @FXML
    private TextField jtfAutor;
    @FXML
    private TextField jtfEditorial;
    @FXML
    private TextArea jtfSinopsis;
    @FXML
    private DatePicker jdFecha;
    @FXML
    private Button jbFileChooserImg;
    @FXML
    private Button jbFileChooserPDF;
    @FXML
    private Button jbAgregarLibro;

    private File pdf;
    private File img;

    private void setLimitesSpiner(Spinner jsValoracion) {
        jsValoracion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
    }

    public void agregarLibro(ActionEvent actionEvent) throws IOException, SQLException {
        String ISBN = jtfISBN.getText();
        String nombre = jtfNombre.getText();
        String autor = jtfAutor.getText();
        String editorial = jtfEditorial.getText();
        String genero = jtfGenero.getText();
        String sinopsis = jtfSinopsis.getText();
        String valoracion = jsValoracion.getValue().toString();
        String fecha = jdFecha.getValue().toString();

        //Preparamos los ficheros .pdf y .jpg para subirlos como bytes
        byte[] pdfData;
        if(pdf!=null) {
            pdfData = new byte[(int) pdf.length()];
        }else{
            pdfData = null;
        }
        DataInputStream dis1 = new DataInputStream(new FileInputStream(pdf));
        dis1.readFully(pdfData);
        dis1.close();

        byte[] imgData = new byte[(int) img.length()];
        DataInputStream dis2 = new DataInputStream(new FileInputStream(img));
        dis2.readFully(imgData);  // read from file into byte[] array
        dis2.close();


        insertLibro(ISBN, nombre, autor, editorial, genero, sinopsis, valoracion, fecha, pdfData, imgData);
    }

    private void insertLibro(String ISBN, String nombre, String autor, String editorial, String genero, String sinopsis, String valoracion, String fecha, byte[] pdf, byte[] img) throws SQLException {
        String query = "INSERT INTO libros (isbn,nombre,autor,editorial,genero,fechaPublicacion,valoracion,sinopsis,pdf,portada)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement pstmt = MySQLConnect.getInstance().prepareStatement(query)) {
            pstmt.setString(1, ISBN);
            pstmt.setString(2, nombre);
            pstmt.setString(3, autor);
            pstmt.setString(4, editorial);
            pstmt.setString(5, genero);
            pstmt.setDate(6, Date.valueOf(fecha));
            pstmt.setInt(7, Integer.parseInt(valoracion));
            pstmt.setString(8, sinopsis);
            pstmt.setBytes(9, pdf);
            pstmt.setBytes(10, img);

            int insert = pstmt.executeUpdate();
            if (insert > 0) {
                String resultado = "Inserción realizada con éxito en la tabla \"libros\".";
                alert("exito", resultado);
                volverAInicio(resultado);
            } else {
                System.out.println("Error al realizar la inserción.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void buscarPDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            jlPDF.setText(selectedFile.getName());
        } else {
            jlPDF.setText("Selección cancelada.");
        }
        this.pdf = selectedFile;
    }

    public void buscarImagen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            jlImg.setText(selectedFile.getName());
        } else {
            jlImg.setText("Selección cancelada.");
        }
        this.img = selectedFile;
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

    private void volverAInicio(String resultado) {
        Stage stage = (Stage) jbAgregarLibro.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLimitesSpiner(jsValoracion);
    }

}

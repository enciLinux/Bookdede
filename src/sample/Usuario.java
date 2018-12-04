package sample;

public class Usuario {
    private static String nombre;

    public Usuario(String nombre){
        this.nombre = nombre;
    }

    public static String getInstance(){ return nombre;}

}

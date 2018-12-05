package sample;

public class Libro {
    private static String isbn;

    public Libro(String isbn){
        this.isbn = isbn;
    }

    public static String getInstance(){ return isbn;}

    public static String getIsbn() {
        return isbn;
    }
}

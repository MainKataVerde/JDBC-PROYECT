package excepciones;

public class NoStockException extends Throwable {
    public NoStockException(String mensaje) {
        super(mensaje);
    }
}

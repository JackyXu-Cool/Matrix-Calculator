package sample;

public class NotMarkovMatrixException extends RuntimeException{
    public NotMarkovMatrixException() {}
    public NotMarkovMatrixException(String message) {
        super(message);
    }
}

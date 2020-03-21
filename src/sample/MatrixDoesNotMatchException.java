package sample;

public class MatrixDoesNotMatchException extends RuntimeException{
    public MatrixDoesNotMatchException() {}
    public MatrixDoesNotMatchException(String message) {
        super(message);
    }
}

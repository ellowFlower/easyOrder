package at.ac.tuwien.sepm.groupphase.backend.exception;


public class AlreadyRegisteredExeption extends RuntimeException {

    public AlreadyRegisteredExeption() {
    }

    public AlreadyRegisteredExeption(String message) {
        super(message);
    }

    public AlreadyRegisteredExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRegisteredExeption(Exception e) {
        super(e);
    }
}

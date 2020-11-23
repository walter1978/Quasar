package rebel.alliance.comm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputDataException extends RuntimeException {
    public InvalidInputDataException() {
        super("The input data is not valid");
    }
}

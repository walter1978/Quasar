package rebel.alliance.comm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LocationProcessingException extends RuntimeException {
    public LocationProcessingException() {
        super("The location couldn't be calculated");
    }
}

package rebel.alliance.comm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MessageProcessingException extends RuntimeException {
    public MessageProcessingException() {
        super("The message couldn't be calculated");
    }
}

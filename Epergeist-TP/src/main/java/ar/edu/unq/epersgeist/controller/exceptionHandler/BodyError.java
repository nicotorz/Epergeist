package ar.edu.unq.epersgeist.controller.exceptionHandler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter @Setter @ToString
public class BodyError {
    private HttpStatus status;
    private String message;
    private String path;

    public BodyError(HttpStatus status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}

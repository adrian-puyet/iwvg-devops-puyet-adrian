package es.upm.miw.devops.rest.exceptionshandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoResourceFoundException.class
            // Eliminado ResponseStatusException.class de aquí
    })
    @ResponseBody
    public ErrorMessage noResourceFoundRequest(Exception exception) {
        return new ErrorMessage(new RuntimeException(
                "Ruta no encontrada. Prueba con: **/actuator/info o **/swagger-ui.html o **/v3/api-docs o **/v3/api-docs.yaml"),
                HttpStatus.NOT_FOUND.value());
    }

    // Opcional: Si quieres capturar ResponseStatusException respetando su propio código de estado
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleResponseStatusException(ResponseStatusException exception) {
        HttpStatus status = (HttpStatus) exception.getStatusCode();
        ErrorMessage error = new ErrorMessage(new RuntimeException(exception.getReason()), status.value());
        return ResponseEntity.status(status).body(error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorMessage validationError(MethodArgumentNotValidException exception) {
        return new ErrorMessage(new RuntimeException("Validation failed"), HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            Exception.class
    })
    @ResponseBody
    public ErrorMessage exception(Exception exception) {
        return new ErrorMessage(new RuntimeException("ERROR"), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
package ar.edu.unq.epersgeist.controller.exceptionHandler;

import ar.edu.unq.epersgeist.controller.EspirituControllerREST;
import ar.edu.unq.epersgeist.controller.MediumControllerREST;
import ar.edu.unq.epersgeist.controller.UbicacionControllerREST;
import ar.edu.unq.epersgeist.modelo.exception.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.TransientPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.NoSuchElementException;

@ControllerAdvice(assignableTypes = {EspirituControllerREST.class, MediumControllerREST.class , UbicacionControllerREST.class})
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<BodyError> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BodyError> handleNoSuchElementFoundException(NoSuchElementException ex, HttpServletRequest request) {
        BodyError body = new BodyError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BodyError> handleIlegalArgumenteExecption(IllegalArgumentException ex , HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BodyError> handleNotFoundEntity(EntityNotFoundException ex, HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EspirituNoSeEncuentraEnLaMismaUbicacionException.class)
    public ResponseEntity<BodyError> handleEspirituInDifferentUbication(EspirituNoSeEncuentraEnLaMismaUbicacionException ex , HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EspirituOcupadoException.class)
    public ResponseEntity<BodyError> handleEspirituIsOcupated(EspirituOcupadoException ex , HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExorcistaSinAngelesException.class)
    public ResponseEntity<BodyError> handleEspirituWithoutAngels(ExorcistaSinAngelesException ex, HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MediumSinSuficienteManaException.class)
    public ResponseEntity<BodyError> handleMediumMana(MediumSinSuficienteManaException ex, HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SoloSePuedenInvocarAngelesEnUnSatuario.class)
    public ResponseEntity<BodyError> handleSantuarioOnlyInvocationAngels(SoloSePuedenInvocarAngelesEnUnSatuario ex, HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SoloSePuedenInvocarDemoniosEnUnCementerio.class)
    public ResponseEntity<BodyError> handleCementerioOnlyInvocationDemons(SoloSePuedenInvocarDemoniosEnUnCementerio ex, HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransientPropertyValueException.class)
    public ResponseEntity<BodyError> handleErrorObjectNotPersist( HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.NOT_FOUND, "El objeto referenciado no existe", request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<BodyError> handleDuplicateEntry(HttpServletRequest request){
        BodyError body = new BodyError(HttpStatus.CONFLICT, "Constraint violation duplicate entry", request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<BodyError> handleSQLSyntaxErrorException(SQLSyntaxErrorException ex, HttpServletRequest request) {
        BodyError body = new BodyError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>( body,HttpStatus.NOT_FOUND);
    }
}

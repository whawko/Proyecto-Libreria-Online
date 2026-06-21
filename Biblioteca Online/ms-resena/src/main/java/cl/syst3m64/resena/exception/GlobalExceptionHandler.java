package cl.syst3m64.resena.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — Reseña no encontrada
    @ExceptionHandler(ResenaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResenaNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 400 — Validaciones fallidas (@NotBlank, @Min, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
            errores.put(err.getField(), err.getDefaultMessage())
        );
        errores.put("timestamp", LocalDateTime.now());
        errores.put("status", 400);
        return ResponseEntity.badRequest().body(errores);
    }

    // 400 — Regla de negocio (ej: usuario ya reseñó el libro)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 503 — Feign no puede conectar a otro MS
    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeign(feign.FeignException ex) {
        String msg = ex.status() == 404
            ? "El libro o usuario indicado no existe en el sistema"
            : "Error al comunicarse con otro microservicio: " + ex.getMessage();
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, msg);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}


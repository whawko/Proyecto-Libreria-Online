package cl.syst3m64.resena.exception;

// ── 404 Reseña no encontrada ──────────────────────────────────────
public class ResenaNotFoundException extends RuntimeException {
    public ResenaNotFoundException(Long id) {
        super("Reseña con id " + id + " no encontrada");
    }
}


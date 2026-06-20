package com.libreria.resena.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "resena")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Long idResena;

    // FK hacia ms-libro (solo guardamos el ID, Feign trae el detalle)
    @Column(name = "id_libro", nullable = false)
    private Long idLibro;

    // FK hacia ms-usuario (solo guardamos el ID, Feign trae el detalle)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "comentario", nullable = false, length = 1000)
    private String comentario;

    // Calificación del 1 al 5
    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @Column(name = "fecha_resena")
    private LocalDate fechaResena;

    // ACTIVO, INACTIVO, ELIMINADO
    @Column(name = "estado", length = 20)
    private String estado;

    @PrePersist
    public void prePersist() {
        this.fechaResena = LocalDate.now();
        if (this.estado == null) this.estado = "ACTIVO";
    }
}

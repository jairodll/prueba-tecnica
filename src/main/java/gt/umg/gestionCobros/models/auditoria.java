package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "auditoria", schema = "dbo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @Column(name = "evento", nullable = false, length = 100)
    private String evento;

    @Column(name = "resumen_payload", columnDefinition = "TEXT")
    private String resumenPayload;

    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "usuario_sistema", length = 100)
    private String usuarioSistema;

    @Column(name = "entidad", length = 50)
    private String entidad;

    @Column(name = "id_entidad")
    private Integer idEntidad;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = new Date();
        }
    }
}
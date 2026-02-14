package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cobros", schema = "dbo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class cobros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cobro")
    private Integer idCobro;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "moneda", nullable = false, length = 3)
    private String moneda = "GTQ";

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "fecha_proceso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProceso;

    @Column(name = "referencia_externa", length = 100)
    private String referenciaExterna;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = new Date();
        }
        if (estado == null) {
            estado = "PENDIENTE";
        }
        if (moneda == null) {
            moneda = "GTQ";
        }
    }
}
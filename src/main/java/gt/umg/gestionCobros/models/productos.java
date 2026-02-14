package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "productos", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
public class productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "descripcion", nullable = true, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "cantidad", nullable = true)
    private Integer cantidad;

    @Column(name = "precio", nullable = true, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "fecha_ingreso", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;

    @Column(name = "fecha_vencimiento", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;

    @Column(name = "usuario_modifico", nullable = true)
    private Integer usuarioModifico;

    @Column(name = "id_usuario", nullable = true)
    private Integer idUsuario;
}

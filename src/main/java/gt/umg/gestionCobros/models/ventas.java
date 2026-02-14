package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ventas", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
public class ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta", nullable = false)
    private Integer idVenta;

    @Column(name = "fecha_venta", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaVenta;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "id_catalogo", nullable = false)
    private Integer idCatalogo;
}

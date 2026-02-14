package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "compras", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
public class compras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra", nullable = false)
    private Integer idCompra;

    @Column(name = "fecha_compra", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;

    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;
}

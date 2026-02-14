package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "devoluciones", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
public class devoluciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucion", nullable = false)
    private Integer idDevolucion;

    @Column(name = "id_venta", nullable = false)
    private Integer idVenta;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "cantidad_devuelta", nullable = false)
    private Integer cantidadDevuelta;

    @Column(name = "motivo_devolucion", nullable = false, length = 255)
    private String motivoDevolucion;

    @Column(name = "fecha_devolucion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
}

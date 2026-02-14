package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "catalogos", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
public class catalogos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo", nullable = false)
    private Integer idCatalogo;

    @Column(name = "id_proveedor", nullable = true)
    private Integer idProveedor;

    @Column(name = "nombre_catalogo", nullable = false, length = 50)
    private String nombreCatalogo;

    @Column(name = "descripcion_catalogo", nullable = true, columnDefinition = "TEXT")
    private String descripcionCatalogo;

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;

    @Column(name = "fecha_modificacion", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;

    @Column(name = "id_tipo_catalogo", nullable = true)
    private Integer idTipoCatalogo;
}

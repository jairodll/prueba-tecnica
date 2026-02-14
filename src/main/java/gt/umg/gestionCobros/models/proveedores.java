package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "proveedores", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
public class proveedores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "direccion", nullable = true, columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "telefono", nullable = true, length = 20)
    private String telefono;

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;

    @Column(name = "usuario_modifico", nullable = true)
    private Integer usuarioModifico;
}

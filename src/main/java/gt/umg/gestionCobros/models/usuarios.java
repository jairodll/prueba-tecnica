package gt.umg.gestionCobros.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "usuarios", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
public class usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "cui", nullable = false, length = 20)
    private String cui;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "correo", nullable = true, length = 50, unique = true)
    private String correo;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;

    @Column(name = "password", nullable = true, length = 50)
    private String password;
}

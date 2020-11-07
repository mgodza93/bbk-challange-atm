package bbk.challenge.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) private String password;

    @Enumerated(EnumType.STRING) private UserType userType;
}

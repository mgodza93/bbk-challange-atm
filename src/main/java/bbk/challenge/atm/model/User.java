package bbk.challenge.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(exclude = {"id", "password"})
@Builder
@ApiObject(name = "User", description = "User entity saved in the database")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    @ApiObjectField
    private String name;

    @ApiObjectField
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) private String password;

    @ApiObjectField
    @Enumerated(EnumType.STRING) private UserType userType;

    public User clone() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .password(this.password)
                .userType(this.userType)
                .build();
    }
}

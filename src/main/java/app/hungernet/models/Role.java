package app.hungernet.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import static app.hungernet.constants.ApplicationConstantsConfigs.ROLE_TABLE_NAME;

@Entity
@Table(name = ROLE_TABLE_NAME)
@Data
public class Role implements Serializable {

    @Id
    @Column(name = "role_name")
    private String roleName;

}

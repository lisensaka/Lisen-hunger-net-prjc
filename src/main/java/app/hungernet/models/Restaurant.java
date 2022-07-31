package app.hungernet.models;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static app.hungernet.constants.ApplicationConstantsConfigs.RESTAURANTS_TABLE_NAME;

@Entity
@Table(name = RESTAURANTS_TABLE_NAME)
@Data
@NoArgsConstructor
public class Restaurant extends BaseEntity {

    private String restaurantName;
    private String restaurantAddress;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Menu> menus;

    @OneToOne
    private User user;
}

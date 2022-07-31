package app.hungernet.models;

import app.hungernet.models.enums.MenuType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static app.hungernet.constants.ApplicationConstantsConfigs.MENU_TABLE_NAME;

@Entity
@Table(name = MENU_TABLE_NAME)
@Data
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private MenuType menuType;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "food_menu",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id"))
    private Set<Food> foods;
}

package app.hungernet.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static app.hungernet.constants.ApplicationConstantsConfigs.FOOD_TABLE_NAME;

@Entity
@Table(name = FOOD_TABLE_NAME)
@Data
@NoArgsConstructor
public class Food extends BaseEntity {

    @Column(unique = true)
    private String foodName;
    private double price;
}

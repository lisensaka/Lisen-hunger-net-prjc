package app.hungernet.models;

import app.hungernet.models.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static app.hungernet.constants.ApplicationConstantsConfigs.ORDER_TABLE_NAME;

@Entity
@Table(name = ORDER_TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private double totalPrice;

    @ManyToOne
    private User user;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "order_foods",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id"))
    private Set<Food> foods;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Restaurant restaurant;
}

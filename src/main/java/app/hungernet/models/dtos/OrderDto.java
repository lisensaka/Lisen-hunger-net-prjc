package app.hungernet.models.dtos;

import app.hungernet.models.enums.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class OrderDto {

    private OrderStatus orderStatus;
    private String restaurantId;
    private Set<FoodDto> foods;

}

package app.hungernet.models.dtos;

import app.hungernet.models.Order;
import app.hungernet.models.enums.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static app.hungernet.models.dtos.FoodDto.convertSetFoodToSetFoodDto;

@Data
@RequiredArgsConstructor
public class OrderDtoForResponse {

    private OrderStatus orderStatus;
    private double totalPrice;
    private String restaurantName;
    private Set<FoodDto> foods;
    private String userName;
    private String orderId;

    public static Page<OrderDtoForResponse> convertListOrderToListOrderDtoForResponse(Page<Order> orders) {
        List<OrderDtoForResponse> orderDtoForResponseList = new ArrayList<>();
        for (Order iOrder : orders) {
            orderDtoForResponseList.add(convertOrderToOrderDtoForResponse(iOrder));
        }
        return new PageImpl<>(orderDtoForResponseList);
    }

    public static OrderDtoForResponse convertOrderToOrderDtoForResponse(Order iOrder) {
        OrderDtoForResponse orderDtoForResponse = new OrderDtoForResponse();
        orderDtoForResponse.setOrderStatus(iOrder.getOrderStatus());
        orderDtoForResponse.setRestaurantName(iOrder.getRestaurant().getRestaurantName());
        orderDtoForResponse.setUserName(iOrder.getUser().getUsername());
        orderDtoForResponse.setTotalPrice(iOrder.getTotalPrice());
        orderDtoForResponse.setFoods(convertSetFoodToSetFoodDto(iOrder.getFoods()));
        orderDtoForResponse.setOrderId(iOrder.getUuid());
        return orderDtoForResponse;
    }
}

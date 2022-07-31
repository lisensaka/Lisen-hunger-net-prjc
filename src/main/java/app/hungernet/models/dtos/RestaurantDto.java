package app.hungernet.models.dtos;

import app.hungernet.models.Restaurant;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static app.hungernet.models.dtos.MenuDto.convertSetMenuToSetMenuDto;

@Data
@RequiredArgsConstructor
public class RestaurantDto {

    private String restaurantName;
    private String restaurantAddress;
    private Set<MenuDto> menus;
    private String restaurantId;

    public static Page<RestaurantDto> convertPageRestaurantToRestaurantDtoPage(Page<Restaurant> restaurants) {
        List<RestaurantDto> restaurantDtos = new ArrayList<>();
        for (Restaurant iRestaurant : restaurants) {
            restaurantDtos.add(convertRestaurantToRestaurantDto(iRestaurant));
        }
        return new PageImpl<>(restaurantDtos);
    }

    public static RestaurantDto convertRestaurantToRestaurantDto(Restaurant iRestaurant) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setRestaurantAddress(iRestaurant.getRestaurantAddress());
        restaurantDto.setRestaurantName(iRestaurant.getRestaurantName());
        restaurantDto.setMenus(convertSetMenuToSetMenuDto(iRestaurant.getMenus()));
        restaurantDto.setRestaurantId(iRestaurant.getUuid());
        return restaurantDto;
    }

    public static void convertRestaurantDtoToRestaurant(Restaurant restaurant, RestaurantDto restaurantDto) {
        restaurant.setRestaurantAddress(restaurantDto.getRestaurantAddress());
        restaurant.setRestaurantName(restaurantDto.getRestaurantName());
    }
}

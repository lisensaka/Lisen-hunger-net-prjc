package app.hungernet.models.dtos;

import app.hungernet.models.Food;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class FoodDto {
    private String foodId;
    private String foodName;
    private double price;

    public static Food convertFoodDtoToFood(FoodDto foodDto) {
        Food food = new Food();
        food.setFoodName(foodDto.getFoodName());
        food.setPrice(foodDto.getPrice());
        food.setUuid(foodDto.getFoodId());
        return food;
    }

    public static Page<FoodDto> convertPageFoodToFoodDtoPage(Page<Food> food) {
        List<FoodDto> foodDtos = new ArrayList<>();
        for (Food iFood : food) {
            foodDtos.add(convertFoodToFoodDto(iFood));
        }
        return new PageImpl<>(foodDtos);
    }

    public static FoodDto convertFoodToFoodDto(Food food) {
        FoodDto foodDto = new FoodDto();
        foodDto.setFoodId(food.getUuid());
        foodDto.setFoodName(food.getFoodName());
        foodDto.setPrice(food.getPrice());
        return foodDto;
    }

    public static Set<Food> convertSetFoodDtoToSetFood(Set<FoodDto> foodDtos) {
        Set<Food> foods = new HashSet<>();
        for (FoodDto iFoodDto : foodDtos) {
            foods.add(convertFoodDtoToFood(iFoodDto));
        }
        return foods;
    }

    public static Set<FoodDto> convertSetFoodToSetFoodDto(Set<Food> foods) {
        Set<FoodDto> foodDtoSet = new HashSet<>();
        for (Food iFood : foods) {
            foodDtoSet.add(convertFoodToFoodDto(iFood));
        }
        return foodDtoSet;
    }
}

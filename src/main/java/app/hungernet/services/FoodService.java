package app.hungernet.services;

import app.hungernet.models.Food;
import app.hungernet.models.dtos.FoodDto;
import app.hungernet.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static app.hungernet.models.dtos.FoodDto.convertFoodDtoToFood;
import static app.hungernet.models.dtos.FoodDto.convertFoodToFoodDto;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Page<FoodDto> getAllFoods(Pageable pageable) throws Exception {
        try {
            return FoodDto.convertPageFoodToFoodDtoPage(foodRepository.findAll(pageable));
        } catch (Exception e) {
            throw new Exception("Error occurred while getting all foods: " + e.getMessage());
        }
    }

    public Food getFoodById(String foodId) {
        try {
            return foodRepository.findById(foodId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Food with id: %s, was not found", foodId));
        }
    }

    public Food addNewFood(FoodDto foodDto) throws Exception {
        try {
            return foodRepository.save(convertFoodDtoToFood(foodDto));
        } catch (Exception e) {
            throw new Exception("Error occurred while adding new food: " + e.getMessage());
        }
    }

    public FoodDto updateFoodById(String foodId, FoodDto foodDto) throws Exception {
        try {
            Food foodById = getFoodById(foodId);
            foodById.setFoodName(foodDto.getFoodName());
            foodById.setPrice(foodDto.getPrice());
            return convertFoodToFoodDto(foodRepository.save(foodById));
        } catch (Exception e) {
            throw new Exception("Error occurred while getting all foods: " + e.getMessage());
        }
    }

    public void deleteFoodById(String foodId) {
        try {
            foodRepository.delete(getFoodById(foodId));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Food with id: %s, was not found", foodId));
        }
    }

}

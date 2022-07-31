package app.hungernet.controllers;

import app.hungernet.models.Food;
import app.hungernet.models.dtos.FoodDto;
import app.hungernet.services.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static app.hungernet.constants.ApplicationConstantsConfigs.FOOD_API;

@RestController
@RequestMapping(FOOD_API)
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/allFoods")
    public ResponseEntity<Page<FoodDto>> getAllFoods(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "createdAt", required = false) String createdAt
    ) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(createdAt)));
        return ResponseEntity.status(HttpStatus.OK).body(foodService.getAllFoods(pageable));
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<Food> getFoodById(@PathVariable String foodId) {
        return ResponseEntity.status(HttpStatus.OK).body(foodService.getFoodById(foodId));
    }

    @PostMapping("/admin-path")
    public ResponseEntity<Food> addNewFood(@RequestBody FoodDto foodDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.addNewFood(foodDto));
    }

    @PutMapping("/admin-path/{foodId}")
    public ResponseEntity<FoodDto> updateFoodById(@PathVariable String foodId, @RequestBody FoodDto foodDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.updateFoodById(foodId, foodDto));
    }

    @DeleteMapping("/admin-path/{foodId}")
    public ResponseEntity<String> deleteFoodById(@PathVariable String foodId) {
        foodService.deleteFoodById(foodId);
        return ResponseEntity.status(HttpStatus.OK).body("Food was deleted successfully");
    }
}

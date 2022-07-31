package app.hungernet.controllers;

import app.hungernet.models.dtos.RestaurantDto;
import app.hungernet.models.dtos.RestaurantManagerDto;
import app.hungernet.models.dtos.RestaurantMenuDto;
import app.hungernet.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static app.hungernet.constants.ApplicationConstantsConfigs.RESTAURANT_API;

@RestController
@RequestMapping(RESTAURANT_API)
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/all")
    public ResponseEntity<Page<?>> getAllRestaurants(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "createdAt", required = false) String createdAt,
            Principal principal
    ) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(createdAt)));
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getAllRestaurant(pageable, principal.getName()));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<?> getRestaurantById(@PathVariable String restaurantId) {
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantById(restaurantId));
    }

    @PostMapping("/admin-path")
    public ResponseEntity<?> addNewRestaurant(@RequestBody RestaurantDto restaurantDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.addNewRestaurant(restaurantDto));
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<?> updateRestaurantById(@PathVariable String restaurantId, @RequestBody RestaurantDto restaurantDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.updateRestaurantById(restaurantId, restaurantDto));
    }

    @DeleteMapping("/admin-path/{restaurantId}")
    public ResponseEntity<String> deleteRestaurantById(@PathVariable String restaurantId) throws Exception {
        restaurantService.deleteRestaurantById(restaurantId);
        return ResponseEntity.status(HttpStatus.OK).body("Restaurant was deleted successfully");
    }

    @PatchMapping("/admin-path")
    public ResponseEntity<?> assignManagerToRestaurant(@RequestBody RestaurantManagerDto restaurantManagerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.assignManagerToRestaurant(restaurantManagerDto));
    }

    @PatchMapping("/assign-menu-to-restaurant/admin-path")
    public ResponseEntity<?> assignMenuToRestaurant(@RequestBody RestaurantMenuDto restaurantMenuDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.assignMenuToRestaurant(restaurantMenuDto));
    }

}

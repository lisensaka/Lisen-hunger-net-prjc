package app.hungernet.controllers;

import app.hungernet.models.dtos.MenuDto;
import app.hungernet.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static app.hungernet.constants.ApplicationConstantsConfigs.MENU_API;

@RestController
@RequestMapping(MENU_API)
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<Page<MenuDto>> getAllMenus(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "createdAt", required = false) String createdAt
    ) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(createdAt)));
        return ResponseEntity.status(HttpStatus.OK).body(menuService.getAllMenus(pageable));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDto> getMenuById(@PathVariable String menuId) {
        return ResponseEntity.status(HttpStatus.OK).body(menuService.getMenuByIdAndConvertToDto(menuId));
    }


    @PostMapping("/admin-path")
    public ResponseEntity<?> addNewMenu(@RequestBody MenuDto menuDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.addNewMenu(menuDto));
    }

    @PutMapping("/admin-path/{menuId}")
    public ResponseEntity<?> updateMenuById(@PathVariable String menuId, @RequestBody MenuDto menuDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.updateMenuById(menuId, menuDto));
    }

    @DeleteMapping("/admin-path/{foodId}")
    public ResponseEntity<String> deleteMenuById(@PathVariable String foodId) throws Exception {
        menuService.deleteMenuById(foodId);
        return ResponseEntity.status(HttpStatus.OK).body("Menu was deleted successfully");
    }
}

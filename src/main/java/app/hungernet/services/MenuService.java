package app.hungernet.services;

import app.hungernet.models.Food;
import app.hungernet.models.Menu;
import app.hungernet.models.dtos.MenuDto;
import app.hungernet.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static app.hungernet.models.dtos.MenuDto.*;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final FoodService foodService;

    public Page<MenuDto> getAllMenus(Pageable pageable) throws Exception {

        try {
            return convertPageMenuToMenuDtoPage(menuRepository.findAll(pageable));
        } catch (Exception e) {
            throw new Exception("Error occurred while getting all menus: " + e.getMessage());
        }
    }

    public MenuDto addNewMenu(MenuDto menuDto) throws Exception {
        try {
            Menu menu = new Menu();
            convertMenuDtoToMenu(menu, menuDto);
            menu.setFoods(getFoodById(menu.getFoods()));
            return convertMenuToMenuDto(menuRepository.save(menu));
        } catch (Exception e) {
            throw new Exception("Error occurred while saving new menu : " + e.getMessage());
        }
    }

    public MenuDto updateMenuById(String menuId, MenuDto menuDto) {
        Menu menuById = findMenuById(menuId);
        convertMenuDtoToMenu(menuById, menuDto);
        return convertMenuToMenuDto(menuRepository.save(menuById));
    }

    public void deleteMenuById(String menuId) throws Exception {
        try {
            menuRepository.delete(findMenuById(menuId));
        } catch (Exception e) {
            throw new Exception("Error occurred while deleting menu : " + e.getMessage());
        }
    }

    public MenuDto getMenuByIdAndConvertToDto(String menuId) {
        return convertMenuToMenuDto(findMenuById(menuId));
    }

    private Set<Food> getFoodById(Set<Food> foodSet) {
        Set<Food> foods = new HashSet<>();
        for (Food iFood : foodSet) {
            foods.add(foodService.getFoodById(iFood.getUuid()));
        }
        return foods;
    }

    public Menu findMenuById(String menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("Menu with id: %s, was not found", menuId)));
    }
}

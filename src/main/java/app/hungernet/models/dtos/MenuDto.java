package app.hungernet.models.dtos;

import app.hungernet.models.Menu;
import app.hungernet.models.enums.MenuType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static app.hungernet.models.dtos.FoodDto.convertSetFoodDtoToSetFood;
import static app.hungernet.models.dtos.FoodDto.convertSetFoodToSetFoodDto;

@Data
@RequiredArgsConstructor
public class MenuDto {
    private MenuType type;
    private String menuId;
    private Set<FoodDto> foods;

    public static void convertMenuDtoToMenu(Menu menu, MenuDto menuDto) {
        menu.setMenuType(menuDto.getType());
        menu.setFoods(convertSetFoodDtoToSetFood(menuDto.getFoods()));
    }

    public static Page<MenuDto> convertPageMenuToMenuDtoPage(Page<Menu> menus) {
        List<MenuDto> menuDtos = new ArrayList<>();
        for (Menu iMenu : menus) {
            menuDtos.add(convertMenuToMenuDto(iMenu));
        }
        return new PageImpl<>(menuDtos);
    }

    public static MenuDto convertMenuToMenuDto(Menu iMenu) {
        MenuDto menuDto = new MenuDto();
        menuDto.setType(iMenu.getMenuType());
        menuDto.setMenuId(iMenu.getUuid());
        menuDto.setFoods(convertSetFoodToSetFoodDto(iMenu.getFoods()));
        return menuDto;
    }

    public static Set<MenuDto> convertSetMenuToSetMenuDto(Set<Menu> menus) {
        Set<MenuDto> menuDtos = new HashSet<>();
        for (Menu iMenu : menus) {
            menuDtos.add(convertMenuToMenuDto(iMenu));
        }
        return menuDtos;
    }
}

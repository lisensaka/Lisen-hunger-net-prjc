package app.hungernet.services;

import app.hungernet.models.Menu;
import app.hungernet.models.Restaurant;
import app.hungernet.models.User;
import app.hungernet.models.dtos.MenuDto;
import app.hungernet.models.dtos.RestaurantDto;
import app.hungernet.models.dtos.RestaurantManagerDto;
import app.hungernet.models.dtos.RestaurantMenuDto;
import app.hungernet.models.enums.MenuType;
import app.hungernet.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static app.hungernet.models.dtos.RestaurantDto.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuService menuService;
    private final UserService userService;

    public Page<?> getAllRestaurant(Pageable pageable, String loggedUsername) throws Exception {
        User loggedUser = getLoadUserByUsername(loggedUsername);
        try {
            Page<Restaurant> allRestaurants = restaurantRepository.findAll(pageable);
            if (!isLoggedUserAdmin(loggedUser)) {
                if (isClient(loggedUser)) {
                    getOnlyActiveMenuForEachRestaurant(allRestaurants);
                    return convertPageRestaurantToRestaurantDtoPage(allRestaurants);
                }
                return restaurantRepository.findAllByUser_UuidLike(loggedUser.getUuid(), pageable);
            }
            return allRestaurants;
        } catch (Exception e) {
            throw new Exception("Error occurred while getting all restaurants: " + e.getMessage());
        }
    }

    private void getOnlyActiveMenuForEachRestaurant(Page<Restaurant> restaurants) throws Exception {
        for (Restaurant iRestaurant : restaurants) {
            iRestaurant.setMenus(checkRestaurantMenus(iRestaurant.getMenus()));
        }
    }

    private Set<Menu> checkRestaurantMenus(Set<Menu> menus) throws Exception {
        Set<Menu> activeMenus = new HashSet<>();
        for (Menu iMenu : menus) {
            if (isMenuActive(iMenu)) {
                activeMenus.add(iMenu);
            }
        }
        return activeMenus;
    }

    private Boolean isMenuActive(Menu iMenu) throws Exception {
        return (iMenu.getMenuType().equals(checkTimeIsForEating()));
    }

    public MenuType checkTimeIsForEating() throws Exception {
        LocalTime localTime = LocalTime.now();
        if (localTime.isAfter(LocalTime.parse("07:00:00")) && localTime.isBefore(LocalTime.parse("11:00:00"))) {
            return MenuType.Breakfast;
        } else if (localTime.isAfter(LocalTime.parse("11:00:00")) && localTime.isBefore(LocalTime.parse("17:00:00"))) {
            return MenuType.Lunch;
        } else if (localTime.isAfter(LocalTime.parse("17:00:00")) && localTime.isBefore(LocalTime.parse("23:59:00"))) {
            return MenuType.Dinner;
        } else {
            throw new Exception("The restaurant is closed");
        }
    }


    public RestaurantDto addNewRestaurant(RestaurantDto restaurantDto) throws Exception {
        try {
            Restaurant restaurant = new Restaurant();
            convertRestaurantDtoToRestaurant(restaurant, restaurantDto);
            addMenuToRestaurantById(restaurantDto.getMenus(), restaurant);
            return convertRestaurantToRestaurantDto(restaurantRepository.save(restaurant));
        } catch (Exception e) {
            throw new Exception("Error occurred while saving new restaurant : " + e.getMessage());
        }
    }

    public Restaurant updateRestaurantById(String restaurantId, RestaurantDto restaurantDto) {
        Restaurant restaurantById = getRestaurantById(restaurantId);
        convertRestaurantDtoToRestaurant(restaurantById, restaurantDto);
        addMenuToRestaurantById(restaurantDto.getMenus(), restaurantById);
        return restaurantRepository.save(restaurantById);
    }

    public void deleteRestaurantById(String restaurantId) throws Exception {
        try {
            restaurantRepository.delete(restaurantRepository.findById(restaurantId).orElseThrow(
                    NoSuchElementException::new
            ));
        } catch (NoSuchElementException e) {
            throw new Exception("Error occurred while deleting restaurant : " + e.getMessage());
        }
    }

    public Restaurant getRestaurantById(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("Restaurant with id: %s, was not found", restaurantId)));
    }

    public Restaurant getRestaurantByIdWithOnlyActiveMenuEnabled(String restaurantId) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("Restaurant with id: %s, was not found", restaurantId)));
        restaurant.setMenus(checkRestaurantMenus(restaurant.getMenus()));
        return restaurant;
    }

    private void addMenuToRestaurantById(Set<MenuDto> menuDtos, Restaurant restaurant) {
        Set<Menu> menus = new HashSet<>();
        for (MenuDto iMenuDto : menuDtos) {
            menus.add(getMenuById(iMenuDto));
        }
        restaurant.setMenus(menus);
    }


    private Menu getMenuById(MenuDto iMenuDto) {
        return menuService.findMenuById(iMenuDto.getMenuId());
    }

    public Restaurant assignManagerToRestaurant(RestaurantManagerDto restaurantManagerDto) {
        User userManager = userService.getUserById(restaurantManagerDto.getUserId());
        Restaurant restaurant = getRestaurantById(restaurantManagerDto.getRestaurantId());
        restaurant.setUser(userManager);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant assignMenuToRestaurant(RestaurantMenuDto restaurantMenuDto) {
        Restaurant restaurant = getRestaurantById(restaurantMenuDto.getRestaurantId());
        Menu menu = menuService.findMenuById(restaurantMenuDto.getMenuId());
        restaurant.setMenus(Set.of(menu));
        return restaurantRepository.save(restaurant);
    }

    private boolean isClient(User loggedUser) {
        return loggedUser.getRole().getRoleName().equals("CLIENT");
    }

    private boolean isLoggedUserAdmin(User loggedUser) {
        return loggedUser.getRole().getRoleName().equals("ADMIN");
    }

    private User getLoadUserByUsername(String loggedUsername) {
        return (User) userService.loadUserByUsername(loggedUsername);
    }
}

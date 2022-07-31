package app.hungernet.services;

import app.hungernet.models.*;
import app.hungernet.models.dtos.FoodDto;
import app.hungernet.models.dtos.OrderDto;
import app.hungernet.models.dtos.OrderDtoForResponse;
import app.hungernet.models.enums.OrderStatus;
import app.hungernet.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static app.hungernet.models.dtos.OrderDtoForResponse.convertListOrderToListOrderDtoForResponse;
import static app.hungernet.models.dtos.OrderDtoForResponse.convertOrderToOrderDtoForResponse;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantService restaurantService;
    private final UserService userService;

    public Page<?> getAllOrders(Pageable pageable, String loggedUsername) throws Exception {
        User loggedUser = getLoggedUser(loggedUsername);
        try {
            if (isLoggedUserClient(loggedUser)) {
                return convertListOrderToListOrderDtoForResponse(orderRepository.getAllByUser_UuidEquals(loggedUser.getUuid(), pageable));
            }
            return convertListOrderToListOrderDtoForResponse(orderRepository.getAllByRestaurant_UserUuidEquals(loggedUser.getUuid(), pageable));
            //  return convertListOrderToListOrderDtoForResponse(orderRepository.findAll(pageable));
        } catch (Exception e) {
            throw new Exception("Error occurred while getting all orders: " + e.getMessage());
        }
    }

    public OrderDtoForResponse addNewOrder(OrderDto orderDto, String loggedUsername) throws Exception {
        Order order = new Order();
        try {
            order.setOrderStatus(OrderStatus.CREATED);
            addRestaurantByIdToOrder(orderDto.getRestaurantId(), order);

            checkRestaurantMenusForTheOrderedFoods(order.getRestaurant().getMenus(), orderDto.getFoods(), order);
            order.setUser(getLoggedUser(loggedUsername));
            return convertOrderToOrderDtoForResponse(orderRepository.save(order));
        } catch (Exception e) {
            throw new Exception("Error occurred while adding new order: " + e.getMessage());
        }
    }

    private void addRestaurantByIdToOrder(String restaurantId, Order order) throws Exception {
        Restaurant restaurantById = restaurantService.getRestaurantByIdWithOnlyActiveMenuEnabled(restaurantId);
        order.setRestaurant(restaurantById);
    }

    private void checkRestaurantMenusForTheOrderedFoods(Set<Menu> menu, Set<FoodDto> orderedFoods, Order order) throws Exception {
        Set<Food> availableFoods = new HashSet<>();
        for (Menu iMenu : menu) {
            checkIfOrderedFoodIsAvailableOnMenu(iMenu.getFoods(), orderedFoods, availableFoods);
        }
        checkIfOrderedFoodsAreNotAvailableOnActiveMenus(availableFoods);
        calculateTotalOrderPrice(order, availableFoods);
        order.setFoods(availableFoods);
    }

    private void checkIfOrderedFoodIsAvailableOnMenu(Set<Food> menuFoods, Set<FoodDto> orderFoods, Set<Food> availableFoods) {
        for (FoodDto iFoodDto : orderFoods) {
            checkByIdIfFoodIsAvailableOnActiveMenu(iFoodDto.getFoodId(), menuFoods, availableFoods);
        }
    }

    private void checkByIdIfFoodIsAvailableOnActiveMenu(String foodId, Set<Food> menuFoods, Set<Food> availableFoods) {
        for (Food iFood : menuFoods) {
            if (iFood.getUuid().equals(foodId)) {
                availableFoods.add(iFood);
            }
        }
    }

    public OrderDtoForResponse updateOrderById(String orderId, OrderDto orderDto, String loggedUserName) throws Exception {
        User loggedUser = getLoggedUser(loggedUserName);
        try {
            Order orderById = getOrderById(orderId);
            updateOrderStatus(orderDto, loggedUser.getRole().getRoleName(), orderById);
            addRestaurantByIdToOrder(orderDto.getRestaurantId(), orderById);
            checkRestaurantMenusForTheOrderedFoods(orderById.getRestaurant().getMenus(), orderDto.getFoods(), orderById);
            // orderById.setFoods(orderDto.getFoods());
            return convertOrderToOrderDtoForResponse(orderRepository.save(orderById));
        } catch (Exception e) {
            throw new Exception("Error occurred while updating order: " + e.getMessage());
        }
    }

    private void updateOrderStatus(OrderDto orderDto, String loggedUserRoleName, Order orderById) throws Exception {
        if ((orderDto.getOrderStatus() != null) && (!orderDto.getOrderStatus().equals(OrderStatus.CREATED))) {
            if (loggedUserRoleName.equals("RESTAURANT_MANAGER")) {
                orderById.setOrderStatus(orderDto.getOrderStatus());
            } else {
                throw new Exception("You cannot update the status of the order");
            }
        }
    }

    public void deleteOrderById(String orderId) throws Exception {
        try {
            orderRepository.delete(getOrderById(orderId));
        } catch (Exception e) {
            throw new Exception("Error occurred while deleting order: " + e.getMessage());
        }
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("Order with id: %s, was not found", orderId)));

    }

    private void calculateTotalOrderPrice(Order order, Set<Food> availableFoods) {
        double totalPrice = 0;
        for (Food iFood : availableFoods) {
            totalPrice += iFood.getPrice();
        }
        order.setTotalPrice(totalPrice);
    }

    public User getLoggedUser(String userName) {
        return (User) userService.loadUserByUsername(userName);
    }

    private boolean isLoggedUserClient(User loggedUser) {
        return loggedUser.getRole().getRoleName().equals("CLIENT");
    }

    public Page<OrderDtoForResponse> filterOrdersByStatus(OrderStatus orderStatus, String loggedUsername, Pageable pageable) throws Exception {
        User loggedUser = getLoggedUser(loggedUsername);
        try {
            if (isLoggedUserClient(loggedUser)) {
                Page<Order> allByOrderStatusEquals = orderRepository.getAllOrdersForSpecificUserFilteredByStatus(loggedUser.getUuid(), orderStatus, pageable);
                return convertListOrderToListOrderDtoForResponse(allByOrderStatusEquals);
            }
            Page<Order> getAllOrdersByStatusForManagers = orderRepository.getAllOrdersForRestaurantManager(loggedUser.getUuid(), orderStatus, pageable);
            List<Order> filteredList = getAllOrdersByStatusForManagers.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
            return convertListOrderToListOrderDtoForResponse(new PageImpl<>(filteredList));
        } catch (Exception e) {
            throw new Exception("Error occurred while filtering orders by status : " + e.getMessage());
        }
    }

    private void checkIfOrderedFoodsAreNotAvailableOnActiveMenus(Set<Food> availableFoods) throws Exception {
        if (availableFoods.isEmpty()) {
            throw new Exception("The food you want are not available or the menu is not available");
        }
    }
}

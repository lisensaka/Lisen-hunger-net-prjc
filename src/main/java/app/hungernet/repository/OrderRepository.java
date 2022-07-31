package app.hungernet.repository;

import app.hungernet.models.Order;
import app.hungernet.models.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> getAllByUser_UuidEquals(String userId, Pageable pageable);

    @Query(value = "select o from Order as o where o.orderStatus = :orderStatus and o.user.uuid = :userId")
    Page<Order> getAllOrdersForSpecificUserFilteredByStatus(@Param("userId") String userId, @Param("orderStatus") OrderStatus orderStatus, Pageable pageable);

    Page<Order> getAllByRestaurant_UserUuidEquals(String restaurantUserId, Pageable pageable);

    @Query(value = "select o from Order as o inner join Restaurant as r on o.restaurant.uuid=r.uuid and r.user.uuid = :userId  and o.orderStatus = :orderStatus")
    Page<Order> getAllOrdersForRestaurantManager(@Param("userId") String userId, @Param("orderStatus") OrderStatus orderStatus, Pageable pageable);
}

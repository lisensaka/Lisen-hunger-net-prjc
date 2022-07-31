package app.hungernet.repository;

import app.hungernet.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    Page<Restaurant> findAllByUser_UuidLike(String userId, Pageable pageable);
}

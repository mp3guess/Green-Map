package tis.techacademy.green_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.model.rating.RatingEntity;
import tis.techacademy.green_map.model.user.UserEntity;

import java.util.List;
import java.util.Optional;
@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    List<RatingEntity> findByPoi(PoiEntity poi);
    Optional<RatingEntity> findByUserAndPoi(UserEntity user, PoiEntity poi);
    @Query("SELECT AVG(r.rating) FROM RatingEntity r WHERE r.poi = :poi")
    Double findAverageRatingByPoi(@Param("poi") PoiEntity poi);
}
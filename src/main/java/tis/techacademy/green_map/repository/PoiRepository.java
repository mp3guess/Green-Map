package tis.techacademy.green_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tis.techacademy.green_map.model.poi.PoiEntity;

@Repository
public interface PoiRepository extends JpaRepository<PoiEntity, Long> {
}

package tis.techacademy.green_map.service.poi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tis.techacademy.green_map.controller.model.poi.PoiRequest;
import tis.techacademy.green_map.model.poi.PoiEntity;

public interface PoiService {
    PoiEntity findPoiById(Long poiId);

    Page<PoiEntity> getAllPois(Pageable pageable);

    PoiEntity addNewPoi(PoiRequest poiRequest);

    void deletePoi(Long poiId);

    PoiEntity updatePoi(Long poiId, PoiRequest updatedPoi);
}

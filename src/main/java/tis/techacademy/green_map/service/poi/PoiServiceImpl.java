package tis.techacademy.green_map.service.poi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tis.techacademy.green_map.controller.model.poi.PoiRequest;
import tis.techacademy.green_map.exception.poi.PoiNotFoundException;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.repository.PoiRepository;
import tis.techacademy.green_map.util.poi.PoiMapper;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PoiServiceImpl implements PoiService {
    private final String POI_NOT_FOUND = "Poi with id %d not found";

    private final PoiRepository poiRepository;

    private final PoiMapper poiMapper;

    @Cacheable(value = "pois", key = "#poiId")
    @Override
    public PoiEntity findPoiById(Long poiId) {
        return poiRepository.findById(poiId).orElseThrow(() -> new PoiNotFoundException(POI_NOT_FOUND.formatted(poiId)));
    }

    @Cacheable(value = "pois")
    @Override
    public Page<PoiEntity> getAllPois(Pageable pageable) {
        return poiRepository.findAll(pageable);
    }

    @Override
    public PoiEntity addNewPoi(PoiRequest poiRequest) {
        PoiDTO poiDTO = poiMapper.requestToDto(poiRequest);
        PoiEntity poiEntity = poiMapper.dtoToEntity(poiDTO);
        return poiRepository.save(poiEntity);
    }

    @CacheEvict(value = "pois", key = "#poiId", allEntries = true)
    @Override
    public void deletePoi(Long poiId) {
        poiRepository.deleteById(poiId);
    }

    @CachePut(value = "pois", key = "#poiId")
    @Override
    public PoiEntity updatePoi(Long poiId, PoiRequest updatedPoi) {
        log.info("Checking if poi with id {} exists", poiId);
        if (!poiRepository.existsById(poiId)) {
            throw new PoiNotFoundException(POI_NOT_FOUND.formatted(poiId));
        }
        PoiDTO poiDTO = poiMapper.requestToDto(updatedPoi);

        poiDTO.setId(poiId);

        PoiEntity poiEntity = poiMapper.dtoToEntity(poiDTO);
        poiRepository.save(poiEntity);

        return poiEntity;
    }
}

package tis.techacademy.green_map.util.poi;

import org.mapstruct.Mapper;
import tis.techacademy.green_map.controller.model.poi.PoiRequest;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.service.poi.PoiDTO;

@Mapper(componentModel = "spring")
public interface PoiMapper {

    PoiEntity dtoToEntity(PoiDTO poiDTO);

    PoiDTO requestToDto(PoiRequest poiRequest);

    PoiEntity requestToEntity(PoiRequest poiRequest);
}
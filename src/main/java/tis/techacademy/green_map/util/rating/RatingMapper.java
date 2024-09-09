package tis.techacademy.green_map.util.rating;

import org.mapstruct.Mapper;
import tis.techacademy.green_map.controller.model.poi.PoiRequest;
import tis.techacademy.green_map.controller.model.rating.RatingRequest;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.model.rating.RatingEntity;
import tis.techacademy.green_map.service.poi.PoiDTO;
import tis.techacademy.green_map.service.rating.RatingDTO;

@Mapper(componentModel = "spring")
public interface RatingMapper{

    RatingEntity requestToEntity(RatingRequest ratingRequest);

    RatingDTO requestToDto(RatingRequest ratingRequest);
}
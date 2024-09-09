package tis.techacademy.green_map.service.rating;

import lombok.Data;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.model.user.UserEntity;

@Data
public class RatingDTO {

    private Long id;

    private UserEntity user;

    private PoiEntity poi;

    private int rating;
}

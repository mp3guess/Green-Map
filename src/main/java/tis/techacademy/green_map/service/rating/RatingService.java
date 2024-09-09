package tis.techacademy.green_map.service.rating;

import tis.techacademy.green_map.controller.model.rating.RatingRequest;
import tis.techacademy.green_map.model.rating.RatingEntity;

import java.util.List;

public interface RatingService {
    RatingEntity addOrUpdateRating(Long poiId, RatingRequest ratingRequest);

    Double getAverageRating(Long poiId);

    List<RatingEntity> getRatingsByPoi(Long poiId);
}

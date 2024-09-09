package tis.techacademy.green_map.service.rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tis.techacademy.green_map.controller.model.rating.RatingRequest;
import tis.techacademy.green_map.exception.poi.PoiNotFoundException;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.model.rating.RatingEntity;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.repository.PoiRepository;
import tis.techacademy.green_map.repository.RatingRepository;
import tis.techacademy.green_map.util.rating.RatingMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final PoiRepository poiRepository;

    @CachePut(value = "ratings", key = "#poiId")
    @Override
    public RatingEntity addOrUpdateRating(Long poiId, RatingRequest ratingRequest) {
        log.info("Adding or updating rating for POI with ID: " + poiId + " and rating: " + ratingRequest.getRating());
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PoiEntity poi = poiRepository.findById(poiId).orElseThrow(() -> new RuntimeException("POI not found"));

        RatingEntity rating = ratingRepository.findByUserAndPoi(currentUser, poi)
                .orElse(new RatingEntity());

        rating.setUser(currentUser);
        rating.setPoi(poi);
        rating.setRating(ratingRequest.getRating());
        rating.setTimestamp(LocalDateTime.now());

        log.info("Saving rating: " + rating);
        return ratingRepository.save(rating);
    }

//    @Cacheable(value = "ratings", key = "'averageRating_' + #poiId")
    @Override
    public Double getAverageRating(Long poiId) {
        log.info("Getting average rating for POI with ID: " + poiId);
        PoiEntity poi = poiRepository.findById(poiId).orElseThrow(() -> new PoiNotFoundException("POI not found"));
        return ratingRepository.findAverageRatingByPoi(poi);
    }

//    @Cacheable(value = "pois", key = "#poiId")
    @Override
    public List<RatingEntity> getRatingsByPoi(Long poiId) {
        log.info("Getting ratings for POI with ID: " + poiId);
        PoiEntity poi = poiRepository.findById(poiId).orElseThrow(() -> new PoiNotFoundException("POI not found"));
        return ratingRepository.findByPoi(poi);
    }
}

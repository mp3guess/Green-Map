package tis.techacademy.green_map.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tis.techacademy.green_map.controller.model.rating.RatingRequest;
import tis.techacademy.green_map.model.rating.RatingEntity;
import tis.techacademy.green_map.service.rating.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/rate/{poiId}")
    public ResponseEntity<RatingEntity> ratePOI(@PathVariable Long poiId,@Valid @RequestBody RatingRequest ratingRequest) {
        log.info("Rating POI with ID: " + poiId + " with rating: " + ratingRequest.getRating() + " is requested.");
        RatingEntity savedRating = ratingService.addOrUpdateRating(poiId, ratingRequest);
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping("/average/{poiId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long poiId) {
        log.info("Getting average rating for POI with ID: " + poiId + " is requested.");
        Double averageRating = ratingService.getAverageRating(poiId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/poi/{poiId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByPoi(@PathVariable Long poiId) {
        log.info("Getting ratings for POI with ID: " + poiId + " is requested.");
        List<RatingEntity> ratings = ratingService.getRatingsByPoi(poiId);
        return ResponseEntity.ok(ratings);
    }
}
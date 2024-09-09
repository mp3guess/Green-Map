package tis.techacademy.green_map.controller.model.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRequest {

    @Min(value = 0, message = "Rating should be between 0 and 5")
    @Max(value = 5, message = "Rating should be between 0 and 5")
    private int rating;
}

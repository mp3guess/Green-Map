package tis.techacademy.green_map.controller.model.poi;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PoiRequest {

    private Long id;

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Category is required")
    private String category;

    private String subcategory;

    private String description;

    @NotEmpty(message = "URL is required")
    private String url;

    @NotEmpty(message = "Email is required")
    private String email;

    private String phoneNumber;

    @NotEmpty(message = "Address is required")
    private String address;

    @Min(value = -180, message = "Longitude should be between -180 and 180")
    @Max(value = 180, message = "Longitude should be between -180 and 180")
    @NotNull(message = "Longitude is required")
    private double longitude;

    @Min(value = -90, message = "Latitude should be between -90 and 90")
    @Max(value = 90, message = "Latitude should be between -90 and 90")
    @NotNull(message = "Latitude is required")
    private double latitude;

    private String openingHours;

    private double averageRating;
}

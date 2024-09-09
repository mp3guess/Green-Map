package tis.techacademy.green_map.controller.model.poi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PoiResponse {
    private Long id;

    private String name;

    private String category;

    private String subcategory;

    private String description;

    private String url;

    private String email;

    private String phoneNumber;

    private String address;

    private double longitude;

    private double latitude;

    private String openingHours;

    private double averageRating;
}

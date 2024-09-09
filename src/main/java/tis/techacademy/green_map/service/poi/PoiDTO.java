package tis.techacademy.green_map.service.poi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoiDTO {
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

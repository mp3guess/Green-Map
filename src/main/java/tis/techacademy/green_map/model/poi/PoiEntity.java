package tis.techacademy.green_map.model.poi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "poi")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoiEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

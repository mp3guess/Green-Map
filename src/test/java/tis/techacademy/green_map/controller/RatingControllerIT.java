package tis.techacademy.green_map.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.repository.PoiRepository;
import tis.techacademy.green_map.repository.RatingRepository;
import tis.techacademy.green_map.service.rating.RatingService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerIT {
    @MockBean
    private PoiRepository poiRepository;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private RatingService ratingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

//    @Test
//    @WithMockUser
//    public void testAddOrUpdateRating_Should_Add_Rating() throws Exception {
//        // Given
//        when(poiRepository.findById(1L)).thenReturn(Optional.ofNullable(poiEntity()));
//        RatingRequest ratingRequest = RatingRequest.builder().rating(5).build();
//        when(ratingService.addOrUpdateRating(1L, ratingRequest)).thenReturn(
//                RatingEntity.builder().id(1L).rating(5).build()
//        );
//
//        // When & Then
//        mockMvc.perform(post("/api/ratings/rate/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ratingRequest))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.rating").value(5));
//    }

    @Test
    @WithMockUser
    public void testGetAverageRating() throws Exception {
        //Given

        PoiEntity poiEntity = PoiEntity.builder()
                .averageRating(0.0)
                .id(1L)
                .build();
        when(poiRepository.findById(1L)).thenReturn(Optional.of(poiEntity));
        when(ratingRepository.findAverageRatingByPoi(poiEntity)).thenReturn(0.0);

        //When
        //Then
        mockMvc.perform(get("/api/ratings/average/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0.0));

    }

    @Test
    @WithMockUser
    public void testGetRatingsByPoi() throws Exception {
        //Given
        when(poiRepository.findById(1L)).thenReturn(Optional.ofNullable(poiEntity()));
        //When
        //Then
        mockMvc.perform(get("/api/ratings/poi/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    private PoiEntity poiEntity() {
        return PoiEntity.builder()
                .id(1L)
                .averageRating(5.0)
                .build();
    }
}


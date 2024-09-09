package tis.techacademy.green_map.service.rating;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import tis.techacademy.green_map.controller.model.rating.RatingRequest;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.model.rating.RatingEntity;
import tis.techacademy.green_map.model.user.Role;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.repository.PoiRepository;
import tis.techacademy.green_map.repository.RatingRepository;
import tis.techacademy.green_map.util.rating.RatingMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RatingServiceImplTest {

    private final Long ID = 1L;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private PoiRepository poiRepository;

    @Mock
    private RatingMapper ratingMapper;


    @InjectMocks
    private RatingServiceImpl underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        // Define the behavior of securityContext and authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(UserEntity.builder()
                .role(Role.ADMIN)
                .email("emailsome")
                .password("passwordsome")
                .id(1)
                .build());

        // Set the SecurityContext in SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser
    void testAddOrUpdateRating_Should_Return_New_Rating() {
        //Given
        RatingRequest ratingRequest = RatingRequest.builder().rating(5).build();
        UserEntity userEntity = UserEntity.builder()
                .role(Role.ADMIN)
                .email("emailsome")
                .password("passwordsome")
                .id(1)
                .build();
        PoiEntity poiEntity = PoiEntity.builder().build();

        RatingEntity ratingEntity = RatingEntity.builder()
                .user(userEntity)
                .poi(poiEntity)
                .rating(5)
                .build();

        when(ratingMapper.requestToEntity(ratingRequest)).thenReturn(ratingEntity);
        when(poiRepository.findById(ID)).thenReturn(Optional.of(poiEntity));
        when(ratingRepository.findByUserAndPoi(userEntity, poiEntity)).thenReturn(Optional.of(ratingEntity));
        when(ratingRepository.save(ratingEntity)).thenReturn(ratingEntity);

        // When
        var actual = underTest.addOrUpdateRating(ID, ratingRequest);

        // Then
        verify(ratingRepository).save(ratingEntity);
        assertEquals(ratingEntity, actual);
    }

    @Test
    void testGetAverageRating_Should_Return_Average_Rating() {
        //Given
        PoiEntity poiEntity = PoiEntity.builder()
                .averageRating(4.5)
                .build();
        when(poiRepository.findById(ID)).thenReturn(Optional.of(poiEntity));
        when(ratingRepository.findAverageRatingByPoi(poiEntity)).thenReturn(4.5);

        //When
        var actual = underTest.getAverageRating(ID);

        //Then
        assertEquals(4.5, actual);
    }

    @Test
    void testGetRatingsByPoi_Should_Return_List_Of_Ratings() {
        //Given
        List<RatingEntity> ratingEntities = List.of(RatingEntity.builder().build(),RatingEntity.builder().build());
        when(poiRepository.findById(ID)).thenReturn(Optional.of(PoiEntity.builder().build()));
        when(ratingRepository.findByPoi(PoiEntity.builder().build())).thenReturn(ratingEntities);
        //When
        var actual = underTest.getRatingsByPoi(ID);
        //Then
        assertEquals(ratingEntities, actual);
    }
}

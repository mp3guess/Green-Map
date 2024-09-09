package tis.techacademy.green_map.service.poi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import tis.techacademy.green_map.controller.model.poi.PoiRequest;
import tis.techacademy.green_map.exception.poi.PoiNotFoundException;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.repository.PoiRepository;
import tis.techacademy.green_map.util.poi.PoiMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

class PoiServiceImplTest {

    private static final long ID = 1L;

    @Mock
    private PoiMapper poiMapper;

    private static final PoiEntity POI_ENTITY = PoiEntity.builder()
            .id(ID)
            .name("Example POI")
            .category("Category 1")
            .url("Example URL")
            .email("Example Email")
            .address("Example Address")
            .longitude(123.456)
            .latitude(242.21)
            .build();

    @Mock
    private PoiRepository poiRepository;

    @InjectMocks
    private PoiServiceImpl underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PoiServiceImpl(poiRepository, poiMapper);
    }

    @Test
    void testFindPoiById_Should_Return_Poi() {
        //Given
        when(poiRepository.findById(ID)).thenReturn(Optional.of(POI_ENTITY));
        //When
        var actual = underTest.findPoiById(ID);
        //Then
        assertNotNull(actual);
        assertEquals(POI_ENTITY, actual);
    }

    @Test
    void testFindPoiById_Should_Not_Return_Poi() {
        //Given
        when(poiRepository.findById(ID)).thenReturn(Optional.empty());
        //When
        //Then
        assertThrows(PoiNotFoundException.class, () -> underTest.findPoiById(ID));
    }

    @Test
    void testGetAllPois_Should_Return_All_Pois() {
        //Given
        PoiEntity poi1 = PoiEntity.builder().build();

        List<PoiEntity> poiList = Collections.singletonList(poi1);
        Page<PoiEntity> poiPage = new PageImpl<>(poiList);

        when(poiRepository.findAll(PageRequest.of(0, 10))).thenReturn(poiPage);

        // When
        Page<PoiEntity> actual = underTest.getAllPois(PageRequest.of(0, 10));

        // Then
        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
    }

    @Test
    void testAddNewPoi_Should_Return_New_Poi() {
        // Given
        PoiRequest poiRequest = poiRequest();
        PoiDTO poiDTO = new PoiDTO();
        PoiEntity expectedEntity = poiEntity();

        when(poiMapper.requestToDto(poiRequest)).thenReturn(poiDTO);
        when(poiMapper.dtoToEntity(poiDTO)).thenReturn(expectedEntity);
        when(poiRepository.save(expectedEntity)).thenReturn(expectedEntity);

        // When
        PoiEntity actual = underTest.addNewPoi(poiRequest);

        // Then
        assertNotNull(actual);
        assertEquals(expectedEntity, actual);
        verify(poiRepository).save(expectedEntity);
    }

    @Test
    void testAddNewPoi_Should_Not_Return_New_Poi() {
        // Given
        when(poiRepository.save(any(PoiEntity.class))).thenReturn(null);

        // When
        PoiEntity actual = underTest.addNewPoi(poiRequest());

        // Then
        assertNull(actual);
    }

    @Test
    void testDeletePoi_Should_Delete_Poi() {
        //Given
        //When
        underTest.deletePoi(ID);

        //Then
        verify(poiRepository).deleteById(ID);
    }

    @Test
    void testDeletePoi_Should_Not_Delete_Poi() {
        //Given

        //When
        underTest.deletePoi(ID);

        //Then
        verify(poiRepository).deleteById(ID);
    }


    @Test
    void testUpdatePoi_Should_Return_Updated_Poi() {
        // Given
        PoiRequest poiRequest = poiRequest();
        PoiDTO poiDTO = PoiDTO.builder().build();
        PoiEntity updatedPoiEntity = poiEntity();
        updatedPoiEntity.setName("Updated POI");

        when(poiRepository.existsById(ID)).thenReturn(true);
        when(poiMapper.requestToDto(poiRequest)).thenReturn(poiDTO);
        when(poiMapper.dtoToEntity(poiDTO)).thenReturn(updatedPoiEntity);
        when(poiRepository.save(updatedPoiEntity)).thenReturn(updatedPoiEntity);

        // When
        PoiEntity actual = underTest.updatePoi(ID, poiRequest());

        // Then
        assertNotNull(actual);
        assertEquals(updatedPoiEntity, actual);
        verify(poiRepository).save(any(PoiEntity.class));
    }

    @Test
    void testUpdatePoi_Should_Not_Return_Updated_Poi() {
        //Given
        when(poiRepository.existsById(ID)).thenReturn(false);

        //When
        //Then
        assertThrows(PoiNotFoundException.class, () -> {
            underTest.updatePoi(ID, poiRequest());
        });
    }

    private PoiRequest poiRequest() {
        return PoiRequest.builder()
                .id(ID)
                .name("Example POI 1")
                .category("Category 1")
                .url("URL 1")
                .email("Email 1")
                .address("Address 1")
                .longitude(123.456)
                .latitude(242.21)
                .build();
    }

    private PoiEntity poiEntity() {
        return PoiEntity.builder()
                .id(ID)
                .name("Example POI 1")
                .category("Category 1")
                .url("URL 1")
                .email("Email 1")
                .address("Address 1")
                .longitude(123.456)
                .latitude(242.21)
                .build();
    }
}

package tis.techacademy.green_map.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tis.techacademy.green_map.controller.model.poi.PoiRequest;
import tis.techacademy.green_map.exception.poi.PoiNotFoundException;
import tis.techacademy.green_map.model.poi.PoiEntity;
import tis.techacademy.green_map.repository.PoiRepository;
import tis.techacademy.green_map.service.poi.PoiServiceImpl;
import tis.techacademy.green_map.util.poi.PoiMapper;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class PoiControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PoiServiceImpl poiService;

    @Autowired
    private PoiRepository poiRepository;

    @Autowired
    private PoiMapper POI_MAPPER;

    @AfterEach
    void tearDown() {
        poiRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetPaginatedData_Should_Return_Paginated_Data() throws Exception {
        //Given
        Page<PoiEntity> poiPage = Page.empty();
        when(poiService.getAllPois(any())).thenReturn(poiPage);

        //When
        //Then
        mockMvc.perform(get("/api/poi/paginated")
                        .param("size", "10")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Transactional
    public void testAddNewPoi_Should_Create_New_Poi() throws Exception {
        //Given
        PoiRequest poiRequest = poiRequest();
        PoiEntity poiEntity = POI_MAPPER.requestToEntity(poiRequest);
        when(poiService.addNewPoi(poiRequest)).thenReturn(POI_MAPPER.requestToEntity(poiRequest));
        when(poiService.findPoiById(1L)).thenReturn(poiEntity);

        //When
        //Then
        mockMvc.perform(post("/api/poi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(poiRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test POI"))
        ;
        Optional<PoiEntity> actual = Optional.ofNullable(poiService.findPoiById(poiEntity.getId()));
        assertTrue(actual.isPresent());
        assertEquals("Test POI", actual.get().getName());

    }

    @Test
    public void testGetPoi_Should_Return_Poi() throws Exception {
        //Given
        PoiEntity poiEntity = new PoiEntity();
        when(poiService.findPoiById(1L)).thenReturn(poiEntity);

        //When
        //Then
        mockMvc.perform(get("/api/poi/{poiId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(poiEntity.getId()));
    }

    @Test
    public void testGetPoi_Should_Return_Throw_Poi_Not_Found_Exception() throws Exception {
        //Given
        when(poiService.findPoiById(100L)).thenThrow(new PoiNotFoundException("POI not found"));

        //When
        //Then
        mockMvc.perform(get("/api/poi/{poiId}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePoi_Should_Update_Poi() throws Exception {
        //Given
        PoiRequest poiRequest = poiRequest();

        PoiEntity poiEntity = PoiEntity.builder()
                .name("Updated POI")
                .description("Test description")
                .build();
        when(poiService.findPoiById(1L)).thenReturn(poiEntity);
        when(poiService.updatePoi(1L, poiRequest)).thenReturn(poiEntity);

        //When
        //Then
        mockMvc.perform(put("/api/poi/{poiId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(poiRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(poiEntity.getId()))
                .andExpect(jsonPath("$.name").value("Updated POI"));

        assertEquals(poiService.findPoiById(1L).getName(), "Updated POI");
    }

    @Test
    public void testDeletePoi_Should_Delete_Poi() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(delete("/api/poi/{poiId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertNull(poiService.findPoiById(1L));
    }

    private PoiRequest poiRequest() {
        return PoiRequest.builder().id(1L)
                .name("Test POI")
                .category("Test category")
                .url("Test url")
                .email("Test email")
                .address("Test address")
                .longitude(1.0)
                .latitude(2.0)
                .build();
    }
}

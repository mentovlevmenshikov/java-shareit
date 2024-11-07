package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserCreateDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ItemRequestControllerTest {

    private static final UserCreateDto VALID_USER = new UserCreateDto(null,"name", "email@mail.ru");
    private static final UserCreateDto VALID_USER_2 = new UserCreateDto(null,"name 2", "email2@mail.ru");
    private static final ItemCreateDto VALID_ITEM = new ItemCreateDto("name", "description", true, null, 1L);
    private static final RequestDto VALID_REQUEST = new RequestDto("description", null);
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetRequestsByRequesterId() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(VALID_USER_2);
        postValidRequest(VALID_REQUEST);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/requests")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].items.length()").value(1))
                .andExpect(jsonPath("$[0].items[0].id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetAllRequests() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(VALID_USER_2);
        postValidRequest(VALID_REQUEST);

        //when
        mockMvc.perform(
                        get("/requests/all")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("description"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andRequestOnGetRequestById() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(VALID_USER_2);
        postValidRequest(VALID_REQUEST);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/requests/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andRequestOnPostRequestWhenValidRequest() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/requests")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(VALID_REQUEST))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnPostRequestWhenInvalidUser() throws Exception {
        //given

        //when
        mockMvc.perform(
                        post("/requests")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(VALID_REQUEST))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isNotFound());
    }

    private void postValidUser(UserCreateDto user) throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidItem(ItemCreateDto item) throws Exception {
        mockMvc.perform(
                post("/items")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidRequest(RequestDto request) throws Exception {
        mockMvc.perform(
                post("/requests")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}

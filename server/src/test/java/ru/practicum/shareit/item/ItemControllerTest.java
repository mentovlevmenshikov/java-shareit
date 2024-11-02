package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ItemControllerTest {

    private static final UserCreateDto VALID_USER = new UserCreateDto(null, "name", "email@mail.ru");
    private static final UserCreateDto VALID_BOOKER = new UserCreateDto(null, "booker", "booker@mail.ru");
    private static final ItemCreateDto VALID_ITEM = new ItemCreateDto("name", "description", true, 2L, null);
    private static final CommentCreateDto VALID_COMMENT = new CommentCreateDto("text", null, null);
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";
    private static final LocalDateTime START = LocalDateTime.now().plusMinutes(10);
    private static final LocalDateTime END = LocalDateTime.now().plusMinutes(11);
    private static final BookingCreateDto VALID_BOOKING = new BookingCreateDto(1L, START, END, null);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetItems() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetItemsPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andEmptyListOnGetItems() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(new UserCreateDto(null, "name 2", "email2@mail.ru"));
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnGetItemWhenValidId() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/1")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemWithBookingsOnGetItemWhenValidId() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(VALID_BOOKER);
        postValidItem(VALID_ITEM);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/items/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.nextBooking.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnGetItemWhenInvalidId() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/99")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPostItemWhenValidItem() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(VALID_ITEM))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnPostItemWhenWrongUserId() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 99)
                                .content(objectMapper.writeValueAsString(VALID_ITEM))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemWhenValidItem() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        patch("/items/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemUpdateDto(1L, "name_updated", "description_updated", false, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name_updated"))
                .andExpect(jsonPath("$.description").value("description_updated"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemNameWhenValidItem() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        patch("/items/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemUpdateDto(null, "name_updated", null, null, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name_updated"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemDescriptionWhenValidItem() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        patch("/items/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemUpdateDto(null, null, "description_updated", null, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description_updated"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemAvailableWhenValidItem() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        patch("/items/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemUpdateDto(null, null, null, false, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn500OnPatchItemWhenNoHeader() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        patch("/items/1")
                                .content(objectMapper.writeValueAsString(new ItemUpdateDto(1L, "name_updated", "description_updated", false, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnSearchItemsByName() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("text", "nAm")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnSearchItemsByDescription() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("text", "eScri")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnSearchItemsByNamePaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("text", "nAm")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnSearchItemsByDescriptionPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("text", "eScri")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andCommentOnPostCommentWhenValidComment() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(VALID_BOOKER);
        postValidItem(VALID_ITEM);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), null));
        patchValidBooking();
        Thread.sleep(5000);

        //when
        mockMvc.perform(
                        post("/items/1/comment")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(VALID_COMMENT))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.authorName").value("booker"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn500OnPostCommentWhenBookingNotFinished() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(VALID_BOOKER);
        postValidItem(VALID_ITEM);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        post("/items/1/comment")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(VALID_COMMENT))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isInternalServerError());
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
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidBooking(BookingCreateDto booking) throws Exception {
        mockMvc.perform(
                post("/bookings")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }


    private void patchValidBooking() throws Exception {
        mockMvc.perform(
                patch("/bookings/1?approved=true")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}

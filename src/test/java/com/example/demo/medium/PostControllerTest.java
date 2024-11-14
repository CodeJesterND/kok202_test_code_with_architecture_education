package com.example.demo.medium;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.post.web.request.PostUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post/post-controller-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/common/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.content").value("hello world"))
                .andExpect(jsonPath("$.writer.id").isNumber())
                .andExpect(jsonPath("$.writer.email").value("dev.hyoseung@gmail.com"))
                .andExpect(jsonPath("$.writer.nickname").value("hyoseung"));
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() throws Exception {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("Hello World")
                .build();

        // when
        // then
        mockMvc.perform(
                put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.content").value("Hello World"))
                .andExpect(jsonPath("$.writer.id").isNumber())
                .andExpect(jsonPath("$.writer.email").value("dev.hyoseung@gmail.com"))
                .andExpect(jsonPath("$.writer.nickname").value("hyoseung"));
    }

}

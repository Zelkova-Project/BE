package backend.zelkova.comment.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.zelkova.ControllerTestSupport;
import backend.zelkova.comment.dto.request.CommentDeleteRequest;
import backend.zelkova.comment.dto.request.CommentUpdateRequest;
import backend.zelkova.comment.dto.request.CommentWriteRequest;
import backend.zelkova.helper.WithAccount;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

class CommentControllerTest extends ControllerTestSupport {

    @Test
    @WithAccount
    @DisplayName("댓글 작성")
    void writeComment() throws Exception {

        // given
        CommentWriteRequest commentWriteRequest = getInstance(CommentWriteRequest.class, Map.of(
                "postId", 1L,
                "comment", "comment"
        ));

        doNothing().when(commentService).write(anyLong(), anyLong(), anyString());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/comments")
                                .content(objectMapper.writeValueAsString(commentWriteRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andDo(document("writeComment",
                        resource(ResourceSnippetParameters.builder()
                                .summary("댓글 작성")
                                .description("댓글을 작성합니다.")
                                .requestFields(
                                        fieldWithPath("postId").description("글 번호"),
                                        fieldWithPath("comment").description("댓글 내용")
                                )
                                .requestSchema(schema("writeCommentRequestForm"))
                                .build())));
    }

    @Test
    @WithAccount
    @DisplayName("댓글 수정")
    void updateComment() throws Exception {

        // given
        CommentUpdateRequest commentUpdateRequest = getInstance(CommentUpdateRequest.class, Map.of(
                "commentId", 1L,
                "content", "content"
        ));

        doNothing().when(commentService).update(anyLong(), anyLong(), anyString());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/comments/update")
                                .content(objectMapper.writeValueAsString(commentUpdateRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isNoContent())
                .andDo(document("updateComment",
                        resource(ResourceSnippetParameters.builder()
                                .summary("댓글 수정")
                                .description("댓글을 수정합니다.")
                                .requestFields(
                                        fieldWithPath("commentId").description("댓글 번호"),
                                        fieldWithPath("content").description("댓글 내용")
                                )
                                .requestSchema(schema("updateCommentRequestForm"))
                                .build())));
    }

    @Test
    @WithAccount
    @DisplayName("댓글 삭제")
    void deleteComment() throws Exception {

        // given
        CommentDeleteRequest commentDeleteRequest = getInstance(CommentDeleteRequest.class, Map.of(
                "commentId", 1L
        ));

        doNothing().when(commentService).delete(anyLong(), anyLong());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/comments/delete")
                                .content(objectMapper.writeValueAsString(commentDeleteRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isNoContent())
                .andDo(document("deleteComment",
                        resource(ResourceSnippetParameters.builder()
                                .summary("댓글 삭제")
                                .description("댓글을 삭제합니다.")
                                .requestFields(
                                        fieldWithPath("commentId").description("댓글 번호")
                                )
                                .requestSchema(schema("deleteCommentRequestForm"))
                                .build())));
    }
}

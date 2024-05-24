package backend.zelkova.post.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.zelkova.ControllerTestSupport;
import backend.zelkova.comment.model.PostCommentResponse;
import backend.zelkova.post.dto.request.PostDeleteRequest;
import backend.zelkova.post.dto.request.PostMoveRequest;
import backend.zelkova.post.dto.request.PostRequest;
import backend.zelkova.post.dto.request.PostUpdateRequest;
import backend.zelkova.post.dto.response.PostAndCommentResponse;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

class PostControllerTest extends ControllerTestSupport {

    @Test
    @WithMockUser
    @DisplayName("글 목록 조회")
    void getPosts() throws Exception {

        // given
        List<PostPreviewResponse> contents = List.of(
                new PostPreviewResponse(1L, "첫번째", LocalDateTime.of(2024, 5, 20, 13, 0)),
                new PostPreviewResponse(2L, "두번째", LocalDateTime.of(2024, 5, 20, 14, 0)),
                new PostPreviewResponse(3L, "세번째", LocalDateTime.of(2024, 5, 20, 15, 0))
        );

        PageImpl<PostPreviewResponse> result = new PageImpl<>(contents, PageRequest.of(0, 20), 3);

        given(postService.getPostPreviews(any()))
                .willReturn(result);

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/posts")
                                .param("page", "0")
                                .param("size", "20")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("postPreviews",
                        resource(ResourceSnippetParameters.builder()
                                .summary("글 미리보기")
                                .description("글 미리보기를 확인합니다.")
                                .queryParameters(
                                        parameterWithName("page").optional().description("페이지 번호"),
                                        parameterWithName("size").optional().description("페이지 당 조회 개수")
                                )
                                .responseFields(
                                        fieldWithPath("content[].no").description("글 id"),
                                        fieldWithPath("content[].title").description("글 제목"),
                                        fieldWithPath("content[].dateTime").description("글 작성 시간"),

                                        fieldWithPath("pageable.sort.empty").description("정렬 데이터 공백 여부"),
                                        fieldWithPath("pageable.sort.sorted").description("정렬 여부"),
                                        fieldWithPath("pageable.sort.unsorted").description("비정렬 여부"),
                                        fieldWithPath("pageable.offset").description("데이터 순번"),
                                        fieldWithPath("pageable.pageNumber").description("페이지 번호"),
                                        fieldWithPath("pageable.pageSize").description("페이지 당 조회 개수"),
                                        fieldWithPath("pageable.paged").description("페이징 정보 포함 여부"),
                                        fieldWithPath("pageable.unpaged").description("페이징 정보 미포함 여부"),

                                        fieldWithPath("last").description("마지막 페이지 여부"),
                                        fieldWithPath("totalPages").description("전체 페이지 개수"),
                                        fieldWithPath("totalElements").description("총 데이터 개수"),
                                        fieldWithPath("first").description("첫 페이지 여부"),
                                        fieldWithPath("size").description("페이지 당 조회 개수"),
                                        fieldWithPath("number").description("현재 페이지 번호"),
                                        fieldWithPath("sort.empty").description("정렬 데이터 공백 여부"),
                                        fieldWithPath("sort.sorted").description("정렬 여부"),
                                        fieldWithPath("sort.unsorted").description("비정렬 여부"),
                                        fieldWithPath("numberOfElements").description("요청 페이지에서 조회된 데이터 개수"),
                                        fieldWithPath("empty").description("데이터 미존재 여부")
                                )
                                .requestSchema(schema("previewPostsRequestForm"))
                                .responseSchema(schema("previewPostsResponseForm"))
                                .build())));
    }

    @Test
    @WithMockUser
    @DisplayName("글 조회")
    void getPost() throws Exception {

        // given
        PostAndCommentResponse result = new PostAndCommentResponse(
                new PostResponse(
                        1L, "관리자", 1L, "첫번째", "내용",
                        LocalDateTime.of(2024, 5, 20, 13, 0),
                        new PostPreviewResponse(null, null, null),
                        new PostPreviewResponse(2L, "두번째",
                                LocalDateTime.of(2024, 5, 20, 14, 0))
                ),
                List.of(new PostCommentResponse(2L, "매니저", 1L, "댓글"))
        );

        given(postService.getPost(anyLong()))
                .willReturn(result);

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/posts/{postId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("postInfo",
                        resource(ResourceSnippetParameters.builder()
                                .summary("글 확인")
                                .description("글을 확인합니다.")
                                .pathParameters(parameterWithName("postId").description("글 id"))
                                .responseFields(
                                        fieldWithPath("postResponse.accountId").description("작성자 id"),
                                        fieldWithPath("postResponse.accountName").description("작성자 이름"),
                                        fieldWithPath("postResponse.no").description("글 id"),
                                        fieldWithPath("postResponse.title").description("글 제목"),
                                        fieldWithPath("postResponse.content").description("글 내용"),
                                        fieldWithPath("postResponse.dateTime").description("글 작성 시각"),

                                        fieldWithPath("postResponse.prev").description("이전글 정보"),
                                        fieldWithPath("postResponse.prev.no").optional().type(NUMBER)
                                                .description("이전글 id"),
                                        fieldWithPath("postResponse.prev.title").optional().type(STRING)
                                                .description("이전글 제목"),
                                        fieldWithPath("postResponse.prev.dateTime").optional().type(STRING)
                                                .description("이전글 작성 시각"),

                                        fieldWithPath("postResponse.next").description("다음글 정보"),
                                        fieldWithPath("postResponse.next.no").optional().type(NUMBER)
                                                .description("다음글 id"),
                                        fieldWithPath("postResponse.next.title").optional().type(STRING)
                                                .description("다음글 제목"),
                                        fieldWithPath("postResponse.next.dateTime").optional().type(STRING)
                                                .description("다음글 작성 시각"),

                                        fieldWithPath("postCommentResponses[].accountId").description("댓글 작성자 id"),
                                        fieldWithPath("postCommentResponses[].name").description("댓글 작성자 이름"),
                                        fieldWithPath("postCommentResponses[].commentId").description("댓글 id"),
                                        fieldWithPath("postCommentResponses[].comment").description("댓글 내용")
                                )
                                .responseSchema(schema("postsResponseForm"))
                                .build())));
    }

    @Test
    @WithMockUser
    @DisplayName("글 작성")
    void writePost() throws Exception {

        // given
        PostRequest postRequest = getInstance(PostRequest.class, Map.of(
                "category", Category.BOARD,
                "visibility", Visibility.PUBLIC,
                "title", "title",
                "content", "content"
        ));

        given(postService.write(any(), any(), any(), anyString(), anyString()))
                .willReturn(1L);

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/posts")
                                .content(objectMapper.writeValueAsString(postRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andDo(document("writePost",
                        resource(ResourceSnippetParameters.builder()
                                .summary("글 작성")
                                .description("글을 작성합니다.")
                                .requestFields(
                                        fieldWithPath("category").description("글 작성 카테고리"),
                                        fieldWithPath("visibility").description(
                                                "글 공개 범위 [PUBLIC: 공개 PRIVATE: 비공개]"),
                                        fieldWithPath("title").description("글 제목"),
                                        fieldWithPath("content").description("글 내용")
                                )
                                .requestSchema(schema("writePostRequestForm"))
                                .build())));
    }

    @Test
    @WithMockUser
    @DisplayName("글 작성")
    void updatePost() throws Exception {

        // given
        PostUpdateRequest postUpdateRequest = getInstance(PostUpdateRequest.class, Map.of(
                "postId", 1L,
                "visibility", Visibility.PRIVATE,
                "title", "첫번째 제목 수정",
                "content", "내용 수정"
        ));

        doNothing().when(postService)
                .update(any(), anyLong(), any(), anyString(), anyString());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/posts")
                                .content(objectMapper.writeValueAsString(postUpdateRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(document("writePost",
                        resource(ResourceSnippetParameters.builder()
                                .summary("글 수정")
                                .description("글을 수정합니다.")
                                .requestFields(
                                        fieldWithPath("postId").description("수정할 글 id"),
                                        fieldWithPath("visibility").description(
                                                "공개 범위 수정 [PUBLIC: 공개 PRIVATE: 비공개]"),
                                        fieldWithPath("title").description("제목 수정"),
                                        fieldWithPath("content").description("내용 수정")
                                )
                                .requestSchema(schema("updatePostRequestForm"))
                                .build())));
    }

    @Test
    @WithMockUser
    @DisplayName("글 카테고리 이동")
    void movePost() throws Exception {

        // given
        PostMoveRequest postMoveRequest = getInstance(PostMoveRequest.class, Map.of(
                "postId", 1L,
                "category", Category.RECRUIT
        ));

        doNothing().when(postService)
                .move(any(), anyLong(), any());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/posts/move")
                                .content(objectMapper.writeValueAsString(postMoveRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(document("movePost",
                        resource(ResourceSnippetParameters.builder()
                                .summary("글 이동")
                                .description("글의 카테고리를 이동합니다. 권한이 필요할 수 있습니다.")
                                .requestFields(
                                        fieldWithPath("postId").description("이동할 글 id"),
                                        fieldWithPath("category").description("이동할 카테고리")
                                )
                                .requestSchema(schema("movePostRequestForm"))
                                .build())));
    }

    @Test
    @WithMockUser
    @DisplayName("글 삭제")
    void deletePost() throws Exception {

        // given
        PostDeleteRequest postDeleteRequest = getInstance(PostDeleteRequest.class, Map.of(
                "postId", 1L
        ));

        doNothing().when(postService)
                .delete(any(), anyLong());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/posts")
                                .content(objectMapper.writeValueAsString(postDeleteRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(document("deletePost",
                        resource(ResourceSnippetParameters.builder()
                                .summary("글 삭제")
                                .description("글을 삭제합니다.")
                                .requestFields(
                                        fieldWithPath("postId").description("삭제할 글 id")
                                )
                                .requestSchema(schema("deletePostRequestForm"))
                                .build())));
    }
}

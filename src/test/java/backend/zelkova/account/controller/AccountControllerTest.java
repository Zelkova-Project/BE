package backend.zelkova.account.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.zelkova.ControllerTestSupport;
import backend.zelkova.account.dto.request.SignupRequestDto;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

class AccountControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception {

        // given
        SignupRequestDto signupRequestDto = getInstance(SignupRequestDto.class, Map.of(
                "loginId", "loginId",
                "password", "password",
                "name", "name",
                "nickname", "nickname",
                "email", "email@email.com"
        ));

        doNothing().when(accountService)
                .signup(anyString(), anyString(), anyString(), anyString(), anyString());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/signup")
                                .content(objectMapper.writeValueAsString(signupRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())

                .andDo(document("signup",
                        resource(ResourceSnippetParameters.builder()
                                .summary("회원 가입")
                                .description("회원 가입을 진행합니다.")
                                .requestFields(
                                        fieldWithPath("loginId").description("로그인 아이디"),
                                        fieldWithPath("password").description("비밀번호"),
                                        fieldWithPath("name").description("사용자명"),
                                        fieldWithPath("nickname").description("사용자 별명"),
                                        fieldWithPath("email").description("이메일")
                                )
                                .requestSchema(schema("signupRequestForm"))
                                .build())));
    }
}

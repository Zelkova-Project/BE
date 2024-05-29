package backend.zelkova.security;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.zelkova.ControllerTestSupport;
import backend.zelkova.account.model.AccountDetail;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

class LoginTest extends ControllerTestSupport {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인")
    void login() throws Exception {

        // given
        given(accountDetailService.loadUserByUsername(anyString()))
                .willReturn(new AccountDetail(1L, "loginId", passwordEncoder.encode("password"), "name", Set.of()));

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/login")
                                .param("loginId", "loginId")
                                .param("password", "password")
                )
                .andExpect(status().isOk())

                .andDo(document("login",
                        resource(ResourceSnippetParameters.builder()
                                .summary("로그인")
                                .description("로그인을 진행합니다.")
                                .formParameters(
                                        parameterWithName("loginId").description("로그인 아이디"),
                                        parameterWithName("password").description("비밀번호")
                                )
                                .requestSchema(schema("loginRequestForm"))
                                .build())));
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFail() throws Exception {

        // given
        given(accountDetailService.loadUserByUsername(anyString()))
                .willThrow(new UsernameNotFoundException("username not found"));

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/login")
                                .param("loginId", "loginId")
                                .param("password", "password")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃")
    void logout() throws Exception {

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/logout")
                )
                .andExpect(status().isOk())

                .andDo(document("logout",
                        resource(ResourceSnippetParameters.builder()
                                .summary("로그아웃")
                                .description("로그아웃을 진행합니다.")
                                .build())));
    }
}

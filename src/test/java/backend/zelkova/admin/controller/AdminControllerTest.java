package backend.zelkova.admin.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.zelkova.ControllerTestSupport;
import backend.zelkova.account.entity.Role;
import backend.zelkova.admin.dto.request.GrantRoleRequest;
import backend.zelkova.admin.dto.request.RemoveRoleRequest;
import backend.zelkova.annotation.WithAdmin;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

class AdminControllerTest extends ControllerTestSupport {

    @Test
    @WithAdmin
    @DisplayName("글 작성")
    void grantRole() throws Exception {

        // given
        GrantRoleRequest grantRoleRequest = getInstance(GrantRoleRequest.class, Map.of(
                "accountId", 2L,
                "role", Role.MANAGER
        ));

        doNothing().when(adminService)
                .grantRole(anyLong(), any());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/roles")
                                .content(objectMapper.writeValueAsString(grantRoleRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(document("grantRole",
                        resource(ResourceSnippetParameters.builder()
                                .summary("권한 부여")
                                .description("특정 사용자에게 권한을 부여합니다.")
                                .requestFields(
                                        fieldWithPath("accountId").description("권한을 부여할 사용자 id"),
                                        fieldWithPath("role").description("부여할 권한")
                                )
                                .requestSchema(schema("grantRoleRequestForm"))
                                .build())));
    }

    @Test
    @WithAdmin
    @DisplayName("권한 제거")
    void removeRole() throws Exception {

        // given
        RemoveRoleRequest removeRoleRequest = getInstance(RemoveRoleRequest.class, Map.of(
                "accountId", 2L,
                "role", Role.MANAGER
        ));

        doNothing().when(adminService)
                .removeRole(anyLong(), any());

        // when
        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/roles")
                                .content(objectMapper.writeValueAsString(removeRoleRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isNoContent())
                .andDo(document("deleteRole",
                        resource(ResourceSnippetParameters.builder()
                                .summary("권한 제거")
                                .description("특정 사용자의 권한을 제거합니다.")
                                .requestFields(
                                        fieldWithPath("accountId").description("권한을 제거할 사용자 id"),
                                        fieldWithPath("role").description("제거할 권한")
                                )
                                .requestSchema(schema("removeRoleRequestForm"))
                                .build())));
    }
}

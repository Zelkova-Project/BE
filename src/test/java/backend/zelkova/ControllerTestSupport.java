package backend.zelkova;

import backend.zelkova.account.service.AccountDetailService;
import backend.zelkova.account.service.AccountService;
import backend.zelkova.admin.service.AdminService;
import backend.zelkova.comment.service.CommentService;
import backend.zelkova.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestConfig.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected AdminService adminService;

    @MockBean
    protected AccountDetailService accountDetailService;

    @MockBean
    protected CommentService commentService;

    @SpyBean
    protected HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor;

    protected <T> T getInstance(Class<T> clazz, Map<String, Object> params) throws Exception {
        Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        T instance = declaredConstructor.newInstance();

        for (Entry<String, Object> entry : params.entrySet()) {
            ReflectionTestUtils.setField(instance, entry.getKey(), entry.getValue());
        }

        return instance;
    }
}

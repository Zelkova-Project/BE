package backend.zelkova;

import backend.zelkova.account.service.AccountService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected PostService postService;

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

package backend.zelkova;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestSupport {

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

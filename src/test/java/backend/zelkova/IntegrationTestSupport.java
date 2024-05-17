package backend.zelkova;

import backend.zelkova.helper.HardDeleteSupplier;
import backend.zelkova.helper.TransactionWrapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({HardDeleteSupplier.class, TransactionWrapper.class})
public abstract class IntegrationTestSupport {

}

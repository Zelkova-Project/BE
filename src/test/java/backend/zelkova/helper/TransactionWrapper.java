package backend.zelkova.helper;

import java.util.function.Supplier;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@Transactional
public class TransactionWrapper {
    public <T> T commit(Supplier<T> supplier) {
        return supplier.get();
    }
}

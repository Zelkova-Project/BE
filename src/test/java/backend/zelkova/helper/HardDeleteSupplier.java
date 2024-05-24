package backend.zelkova.helper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import java.util.Arrays;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestComponent
public class HardDeleteSupplier {

    private final EntityManager entityManager;

    public HardDeleteSupplier(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void hardDelete(Class<?>... classes) {
        Arrays.stream(classes)
                .map(aClass -> aClass.getAnnotation(Table.class))
                .forEach(this::allDelete);
    }

    private void allDelete(Table table) {
        String tableName = table.name();
        entityManager.createNativeQuery("DELETE FROM " + tableName)
                .executeUpdate();
    }
}

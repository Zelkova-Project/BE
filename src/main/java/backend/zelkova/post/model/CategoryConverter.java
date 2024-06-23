package backend.zelkova.post.model;

import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        try {
            return Category.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ExceptionStatus.FAIL_CONVERT);
        }
    }
}

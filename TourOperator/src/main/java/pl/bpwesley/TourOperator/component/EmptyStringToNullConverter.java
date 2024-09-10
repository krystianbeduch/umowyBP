package pl.bpwesley.TourOperator.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmptyStringToNullConverter implements Converter<String, String> {

    @Override
    public String convert(String source) {
        return source.trim().isEmpty() ? null : source;
    }
}

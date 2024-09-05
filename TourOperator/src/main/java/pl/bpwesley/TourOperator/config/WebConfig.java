package pl.bpwesley.TourOperator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.bpwesley.TourOperator.component.StringToNullConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final StringToNullConverter stringToNullConverter;

    public WebConfig(StringToNullConverter stringToNullConverter) {
        this.stringToNullConverter = stringToNullConverter;
    }

    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToNullConverter);
    }

}
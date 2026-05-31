package com.gogidix.corporate.website.infrastructure.config;

import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class I18nConfig {

    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setSupportedLocales(List.of(
                Locale.ENGLISH,
                Locale.FRENCH,
                new Locale("es"),
                new Locale("pt"),
                new Locale("ar")
        ));
        return resolver;
    }

    @Bean
    public Language defaultLanguage() {
        return Language.EN;
    }

    @Bean
    public Region defaultRegion() {
        return Region.NG;
    }
}

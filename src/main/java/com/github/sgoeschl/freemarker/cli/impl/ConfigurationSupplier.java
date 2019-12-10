package com.github.sgoeschl.freemarker.cli.impl;

import com.github.sgoeschl.freemarker.cli.model.Settings;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import java.util.function.Supplier;

import static freemarker.template.Configuration.VERSION_2_3_29;
import static java.util.Objects.requireNonNull;

public class ConfigurationSupplier implements Supplier<Configuration> {

    private static final Version FREEMARKER_VERSION = VERSION_2_3_29;

    private final Settings settings;
    private final Supplier<TemplateLoader> templateLoader;

    public ConfigurationSupplier(Settings settings, Supplier<TemplateLoader> templateLoader) {
        this.settings = requireNonNull(settings);
        this.templateLoader = requireNonNull(templateLoader);
    }

    @Override
    public Configuration get() {
        final Configuration configuration = new Configuration(FREEMARKER_VERSION);
        configuration.setAPIBuiltinEnabled(false);
        configuration.setDefaultEncoding(settings.getTemplateEncoding().name());
        configuration.setLocale(settings.getLocale());
        configuration.setLogTemplateExceptions(false);
        configuration.setObjectWrapper(objectWrapper());
        configuration.setOutputEncoding(settings.getOutputEncoding().name());
        configuration.setTemplateLoader(templateLoader.get());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }

    private static DefaultObjectWrapper objectWrapper() {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(FREEMARKER_VERSION);
        builder.setIterableSupport(false);
        return builder.build();
    }
}

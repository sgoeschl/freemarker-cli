package com.github.sgoeschl.freemarker.cli.util;

import java.util.Locale;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNullOrEmtpty;

public class LocaleUtil {

    public static Locale parseLocale(String value) {
        if (isNullOrEmtpty(value)) {
            return Locale.getDefault();
        }

        final String[] parts = value.split("_");
        return parts.length == 1 ? new Locale(parts[0]) : new Locale(parts[0], parts[1]);
    }
}

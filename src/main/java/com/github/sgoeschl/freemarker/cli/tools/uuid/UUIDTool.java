package com.github.sgoeschl.freemarker.cli.tools.uuid;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UUIDTool {

    public String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public String nameUUIDFromBytes(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes(UTF_8)).toString();
    }
}

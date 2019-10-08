package com.github.sgoeschl.freemarker.cli.tools.snakeyaml;

import java.util.HashMap;
import java.util.Map;

public class SnakeYamlDataModel {

    public Map<String, Object> create() {
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("SnakeYAML", new SnakeYamlParserBean());
        return dataModel;
    }
}

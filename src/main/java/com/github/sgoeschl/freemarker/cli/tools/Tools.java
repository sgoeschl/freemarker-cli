package com.github.sgoeschl.freemarker.cli.tools;

import com.github.sgoeschl.freemarker.cli.model.Settings;
import com.github.sgoeschl.freemarker.cli.tools.commonscsv.CommonsCsvDataModel;
import com.github.sgoeschl.freemarker.cli.tools.environment.EnvironmentDataModel;
import com.github.sgoeschl.freemarker.cli.tools.excel.ExcelDataModel;
import com.github.sgoeschl.freemarker.cli.tools.freemarker.FreeMarkerDataModel;
import com.github.sgoeschl.freemarker.cli.tools.jsonpath.JsonPathDataModel;
import com.github.sgoeschl.freemarker.cli.tools.jsoup.JsoupDataModel;
import com.github.sgoeschl.freemarker.cli.tools.propertiesparser.PropertiesParserDataModel;
import com.github.sgoeschl.freemarker.cli.tools.reportdata.ReportDataModel;
import com.github.sgoeschl.freemarker.cli.tools.systemproperties.SystemPropertiesDataModel;
import com.github.sgoeschl.freemarker.cli.tools.xml.XmlParserDataModel;

import java.util.HashMap;
import java.util.Map;

public class Tools {

    private final Settings settings;

    public Tools(Settings settings) {
        this.settings = settings;
    }

    public Map<String, Object> create() {
        final Map<String, Object> dataModel = new HashMap<>();

        dataModel.putAll(new EnvironmentDataModel().create());
        dataModel.putAll(new FreeMarkerDataModel().create());
        dataModel.putAll(new ReportDataModel().create(settings.getDescription()));
        dataModel.putAll(new SystemPropertiesDataModel().create());

        dataModel.putAll(new CommonsCsvDataModel().create());
        dataModel.putAll(new ExcelDataModel().create());
        dataModel.putAll(new JsonPathDataModel().create());
        dataModel.putAll(new JsoupDataModel().create());
        dataModel.putAll(new PropertiesParserDataModel().create());
        dataModel.putAll(new XmlParserDataModel().create());

        return dataModel;
    }
}

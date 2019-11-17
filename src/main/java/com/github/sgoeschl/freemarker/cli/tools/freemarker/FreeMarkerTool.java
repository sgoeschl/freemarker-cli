package com.github.sgoeschl.freemarker.cli.tools.freemarker;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import freemarker.template.utility.ObjectConstructor;

public class FreeMarkerTool {

    final BeansWrapper beansWrapper;
    final ObjectConstructor objectConstructor;

    public FreeMarkerTool() {
        final BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_29);
        this.beansWrapper = builder.build();
        this.objectConstructor = new ObjectConstructor();
    }

    public ObjectConstructor getObjectConstructor() {
        return objectConstructor;
    }

    public TemplateHashModel getEnums() {
        return beansWrapper.getEnumModels();
    }

    public TemplateHashModel getStatics() {
        return beansWrapper.getStaticModels();
    }
}



package com.github.sgoeschl.freemarker.cli.tools.system;

import com.github.sgoeschl.freemarker.cli.model.Settings;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

public class SystemTool {

    private final Settings settings;

    public SystemTool(Settings settings) {
        this.settings = settings;
    }

    public Properties getProperties() {
        return System.getProperties();
    }

    public String getProperty(String key) {
        return System.getProperty(key);
    }

    public String getProperty(String key, String def) {
        return System.getProperty(key, def);
    }

    public Map<String, String> getEnvironment() {
        return System.getenv();
    }

    public String getEnvironment(String name) {
        return System.getenv(name);
    }

    public String getEnvironment(String name, String def) {
        return System.getenv(name) != null ? System.getenv(name) : def;
    }

    public Settings getSettings() {
        return settings;
    }

    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
            return "localhost";
        }
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}

package com.bedrockcloud.bedrockcloud.templates;

import java.io.IOException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.files.json.json;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import java.util.HashMap;
import java.util.Map;

public class TemplateProvider implements Loggable
{
    public HashMap<String, Template> templateMap;
    public HashMap<String, Template> runningTemplates;
    
    public TemplateProvider() {
        this.templateMap = new HashMap<String, Template>();
        this.runningTemplates = new HashMap<String, Template>();
    }
    
    public HashMap<String, Template> getRunningTemplates() {
        return this.runningTemplates;
    }
    
    public HashMap<String, Template> getTemplateMap() {
        return this.templateMap;
    }
    
    public void addTemplate(final Template template) {
        this.templateMap.put(template.getName(), template);
    }
    
    public Template getTemplate(final String name) {
        return this.templateMap.get(name);
    }
    
    public boolean existsTemplate(final String name) {
        return this.templateMap.get(name) != null;
    }
    
    public void removeTemplate(final String name) {
        this.templateMap.remove(name);
    }
    
    public void removeTemplate(final Template template) {
        this.templateMap.remove(template.getName());
    }
    
    public boolean isTemplateRunning(final Template template) {
        return this.runningTemplates.containsKey(template.getName());
    }
    
    public void addRunningTemplate(final Template template) {
        this.runningTemplates.put(template.getName(), template);
    }
    
    public void removeRunningGroup(final String name) {
        this.runningTemplates.remove(name);
    }
    
    public void removeRunningGroup(final Template group) {
        this.runningTemplates.remove(group.getName());
    }
    
    public void loadTemplates() {
        for (final String name : GroupAPI.getGroups()) {
            try {
                final HashMap<String, Object> stats = (HashMap<String, Object>) json.get(name, 10);
                new Template(name, Math.toIntExact((Long) stats.get("minRunningServer")), Math.toIntExact((Long) stats.get("maxRunningServer")), Math.toIntExact((Long) stats.get("maxPlayer")), Math.toIntExact((Long) stats.get("type")), (Boolean) stats.get("beta"), (Boolean) stats.get("maintenance"), (Boolean) stats.get("isLobby"), (Boolean) stats.get("canBePrivate"), (Boolean) stats.get("isStatic"));
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }
}

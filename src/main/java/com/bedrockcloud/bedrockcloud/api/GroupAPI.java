package com.bedrockcloud.bedrockcloud.api;

import java.util.Objects;
import java.io.IOException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.File;

public class GroupAPI implements Loggable
{
    public static final int PROXY_SERVER = 0;
    public static final int POCKETMINE_SERVER = 1;
    
    public static boolean isGroup(final String group) {
        final File theDir = new File("./templates/" + group);
        return theDir.exists();
    }
    
    public static void createGroup(final String name, final int type) {
        if (!isGroup(name)) {
            if (type == 1) {
                try {
                    final ArrayList<String> directorys = new ArrayList<String>();
                    directorys.add("./templates/" + name);
                    directorys.add(directorys.get(0) + "/crashdumps");
                    directorys.add(directorys.get(0) + "/plugins");
                    directorys.add(directorys.get(0) + "/plugin_data");
                    directorys.add(directorys.get(0) + "/worlds");
                    directorys.add(directorys.get(0) + "/server.properties");
                    for (final String direc : directorys) {
                        final File theDir = new File(direc);
                        if (!theDir.exists()) {
                            if (!direc.equals(directorys.get(0) + "/server.properties")) {
                                theDir.mkdirs();
                            } else {
                                theDir.createNewFile();
                                final FileWriter writer = new FileWriter(direc, true);
                                final JSONParser jsonParser = new JSONParser();
                                JSONArray template = new JSONArray();
                                try {
                                    final FileReader reader = new FileReader("./templates/config.json");
                                    try {
                                        final Object obj = jsonParser.parse(reader);
                                        if (obj.equals("")) {
                                            template = new JSONArray();
                                        } else {
                                            template = (JSONArray)obj;
                                        }
                                        reader.close();
                                    } catch (Throwable throwable) {
                                        try {
                                            reader.close();
                                        } catch (Throwable throwable2) {
                                            throwable.addSuppressed(throwable2);
                                        }
                                        throw throwable;
                                    }
                                } catch (ParseException ignored) {}
                                final JSONObject object = new JSONObject();
                                object.put("minRunningServer", 1);
                                object.put("maxRunningServer", 10);
                                object.put("maxPlayer", 100);
                                object.put("maintenance", false);
                                object.put("beta", true);
                                object.put("isLobby", false);
                                object.put("canBePrivate", false);
                                object.put("proxy", "Proxy-Master");
                                object.put("type", 1);
                                final JSONObject temp = new JSONObject();
                                temp.put(name, object);
                                template.add(temp);
                                final FileWriter fileWriter = new FileWriter("./templates/config.json");
                                fileWriter.write(template.toJSONString());
                                fileWriter.flush();
                            }
                        }
                    }
                    BedrockCloud.getLogger().debug("The Group " + name + " has successfully been created!");
                } catch (IOException e) {
                    BedrockCloud.getLogger().exception(e);
                }
            } else {
                try {
                    final ArrayList<String> directorys = new ArrayList<String>();
                    directorys.add("./templates/" + name);
                    for (final String direc : directorys) {
                        final File theDir = new File(direc);
                        if (!theDir.exists()) {
                            theDir.mkdirs();
                            final JSONParser jsonParser2 = new JSONParser();
                            JSONArray template2 = new JSONArray();
                            try {
                                final FileReader reader2 = new FileReader("./templates/config.json");
                                try {
                                    final Object obj2 = jsonParser2.parse(reader2);
                                    if (obj2.equals("")) {
                                        template2 = new JSONArray();
                                    } else {
                                        template2 = (JSONArray)obj2;
                                    }
                                    reader2.close();
                                } catch (Throwable throwable3) {
                                    try {
                                        reader2.close();
                                    } catch (Throwable throwable4) {
                                        throwable3.addSuppressed(throwable4);
                                    }
                                    throw throwable3;
                                }
                            } catch (ParseException ignored) {}
                            final JSONObject object2 = new JSONObject();
                            object2.put("minRunningServer", 1);
                            object2.put("maxRunningServer", 10);
                            object2.put("maxPlayer", 100);
                            object2.put("maintenance", false);
                            object2.put("type", 0);
                            final JSONObject temp2 = new JSONObject();
                            temp2.put(name, object2);
                            template2.add(temp2);
                            final FileWriter fileWriter2 = new FileWriter("./templates/config.json");
                            fileWriter2.write(template2.toJSONString());
                            fileWriter2.flush();
                        }
                    }
                    BedrockCloud.getLogger().debug("The Group " + name + " has successfully been created!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static ArrayList<String> getGroups() {
        final File file = new File("./templates/");
        final ArrayList<String> names = new ArrayList<String>();
        for (final File own : Objects.requireNonNull(file.listFiles())) {
            if (!own.getName().equals("config.json")) {
                names.add(own.getName());
            }
        }
        return names;
    }
}

package com.bedrockcloud.bedrockcloud.files.json;

import java.io.IOException;
import java.util.Iterator;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import org.json.simple.JSONObject;
import java.util.HashMap;
import org.json.simple.JSONArray;
import java.io.Reader;
import java.io.FileReader;
import org.json.simple.parser.JSONParser;

public class json
{
    public static final int minRunningServer = 0;
    public static final int maxRunningServer = 1;
    public static final int maxPlayer = 2;
    public static final int maintenance = 3;
    public static final int beta = 4;
    public static final int canBePrivate = 5;
    public static final int proxy = 6;
    public static final int type = 7;
    public static final int isLobby = 8;
    public static final int ALL = 9;
    
    public static Object get(final String name, final int type) throws IOException {
        final JSONParser jsonParser = new JSONParser();
        try {
            final FileReader reader = new FileReader("./templates/config.json");
            try {
                final Object obj = jsonParser.parse(reader);
                final JSONArray template = (JSONArray)obj;
                final Object[] returnval = { null };
                final HashMap<String, Object> stats = new HashMap<String, Object>();
                for (final Object tmp : template) {
                    final JSONObject tempobj = (JSONObject)tmp;
                    final JSONObject directtemp = (JSONObject) tempobj.get(name);
                    if (directtemp != null) {
                        if (type != 9) {
                            switch (type) {
                                case 0: {
                                    returnval[0] = directtemp.get("minRunningServer");
                                    continue;
                                }
                                case 1: {
                                    returnval[0] = directtemp.get("maxRunningServer");
                                    continue;
                                }
                                case 2: {
                                    returnval[0] = directtemp.get("maxPlayer");
                                    continue;
                                }
                                case 3: {
                                    returnval[0] = directtemp.get("maintenance");
                                    continue;
                                }
                                case 4: {
                                    returnval[0] = directtemp.get("beta");
                                    continue;
                                }
                                case 5: {
                                    returnval[0] = directtemp.get("canBePrivate");
                                    continue;
                                }
                                case 6: {
                                    returnval[0] = directtemp.get("proxy");
                                    continue;
                                }
                                case 7: {
                                    returnval[0] = directtemp.get("type");
                                    continue;
                                }
                                case 8: {
                                    returnval[0] = directtemp.get("isLobby");
                                    continue;
                                }
                                default: {
                                }
                            }
                        } else {
                            stats.put("minRunningServer", directtemp.get("minRunningServer"));
                            stats.put("maxRunningServer", directtemp.get("maxRunningServer"));
                            stats.put("maxPlayer", directtemp.get("maxPlayer"));
                            stats.put("maintenance", directtemp.get("maintenance"));
                            stats.put("beta", directtemp.get("beta"));
                            stats.put("canBePrivate", directtemp.get("canBePrivate"));
                            stats.put("proxy", directtemp.get("proxy"));
                            stats.put("type", directtemp.get("type"));
                            stats.put("isLobby", directtemp.get("isLobby"));
                        }
                    }
                }
                if (stats.isEmpty()) {
                    final Object object = returnval[0];
                    reader.close();
                    return object;
                }
                final HashMap<String, Object> hashMap1 = stats;
                reader.close();
                return hashMap1;
            }
            catch (Throwable throwable) {
                try {
                    reader.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
        }
        catch (FileNotFoundException | ParseException ex2) {
            final Exception ex = new Exception();
            final Exception e = ex;
            e.printStackTrace();
            return false;
        }
    }
}

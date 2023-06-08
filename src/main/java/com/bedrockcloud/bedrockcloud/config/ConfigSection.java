package com.bedrockcloud.bedrockcloud.config;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

public class ConfigSection extends LinkedHashMap<String, Object>
{
    public ConfigSection() {
    }
    
    public ConfigSection(final String key, final Object value) {
        this();
        this.set(key, value);
    }
    
    public ConfigSection(final LinkedHashMap<String, Object> map) {
        this();
        if (map == null || map.isEmpty()) {
            return;
        }
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof LinkedHashMap) {
                super.put(entry.getKey(), new ConfigSection((LinkedHashMap<String, Object>) entry.getValue()));
            }
            else {
                super.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public Map<String, Object> getAllMap() {
        final LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.putAll((Map<? extends String, ?>) this);
        return map;
    }
    
    public ConfigSection getAll() {
        return new ConfigSection(this);
    }
    
    public Object get(final String key) {
        return this.get(key, (Object)null);
    }
    
    public <T> T get(final String key, final T defaultValue) {
        if (key == null || key.isEmpty()) {
            return defaultValue;
        }
        if (super.containsKey(key)) {
            return (T)super.get(key);
        }
        final String[] keys = key.split("\\.", 2);
        if (!super.containsKey(keys[0])) {
            return defaultValue;
        }
        final Object value = super.get(keys[0]);
        if (value != null && value instanceof ConfigSection) {
            final ConfigSection section = (ConfigSection)value;
            return (T)section.get(keys[1], (Object)defaultValue);
        }
        return defaultValue;
    }
    
    public void set(final String key, final Object value) {
        final String[] subKeys = key.split("\\.", 2);
        if (subKeys.length > 1) {
            ConfigSection childSection = new ConfigSection();
            if (this.containsKey(subKeys[0]) && super.get(subKeys[0]) instanceof ConfigSection) {
                childSection = (ConfigSection) super.get(subKeys[0]);
            }
            childSection.set(subKeys[1], value);
            super.put(subKeys[0], childSection);
        }
        else {
            super.put(subKeys[0], value);
        }
    }
    
    public boolean isSection(final String key) {
        final Object value = this.get(key);
        return value instanceof ConfigSection;
    }
    
    public ConfigSection getSection(final String key) {
        return this.get(key, new ConfigSection());
    }
    
    public ConfigSection getSections() {
        return this.getSections(null);
    }
    
    public ConfigSection getSections(final String key) {
        final ConfigSection sections = new ConfigSection();
        final ConfigSection parent = (key == null || key.isEmpty()) ? this.getAll() : this.getSection(key);
        if (parent == null) {
            return sections;
        }
        final HashMap<String, Object> hashMap = null;
        parent.entrySet().forEach(e -> {
            if (e.getValue() instanceof ConfigSection) {
                hashMap.put(e.getKey(), e.getValue());
            }
            return;
        });
        return sections;
    }
    
    public int getInt(final String key) {
        return this.getInt(key, 0);
    }
    
    public int getInt(final String key, final int defaultValue) {
        return this.get(key, defaultValue).intValue();
    }
    
    public boolean isInt(final String key) {
        final Object val = this.get(key);
        return val instanceof Integer;
    }
    
    public long getLong(final String key) {
        return this.getLong(key, 0L);
    }
    
    public long getLong(final String key, final long defaultValue) {
        return this.get(key, defaultValue).longValue();
    }
    
    public boolean isLong(final String key) {
        final Object val = this.get(key);
        return val instanceof Long;
    }
    
    public double getDouble(final String key) {
        return this.getDouble(key, 0.0);
    }
    
    public double getDouble(final String key, final double defaultValue) {
        return this.get(key, defaultValue).doubleValue();
    }
    
    public boolean isDouble(final String key) {
        final Object val = this.get(key);
        return val instanceof Double;
    }
    
    public String getString(final String key) {
        return this.getString(key, "");
    }
    
    public String getString(final String key, final String defaultValue) {
        final Object result = this.get(key, defaultValue);
        return String.valueOf(result);
    }
    
    public boolean isString(final String key) {
        final Object val = this.get(key);
        return val instanceof String;
    }
    
    public boolean getBoolean(final String key) {
        return this.getBoolean(key, false);
    }
    
    public boolean getBoolean(final String key, final boolean defaultValue) {
        return this.get(key, defaultValue);
    }
    
    public boolean isBoolean(final String key) {
        final Object val = this.get(key);
        return val instanceof Boolean;
    }
    
    public List getList(final String key) {
        return this.getList(key, null);
    }
    
    public List getList(final String key, final List defaultList) {
        return this.get(key, defaultList);
    }
    
    public boolean isList(final String key) {
        final Object val = this.get(key);
        return val instanceof List;
    }
    
    public List<String> getStringList(final String key) {
        final List value = this.getList(key);
        if (value == null) {
            return new ArrayList<String>(0);
        }
        final List<String> result = new ArrayList<String>();
        for (final Object o : value) {
            if (o instanceof String || o instanceof Number || o instanceof Boolean || o instanceof Character) {
                result.add(String.valueOf(o));
            }
        }
        return result;
    }
    
    public List<Integer> getIntegerList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Integer>(0);
        }
        final List<Integer> result = new ArrayList<Integer>();
        for (final Object object : list) {
            if (object instanceof Integer) {
                result.add((Integer)object);
            }
            else if (object instanceof String) {
                try {
                    result.add(Integer.valueOf((String)object));
                }
                catch (Exception ex) {}
            }
            else if (object instanceof Character) {
                result.add((int)(char)object);
            }
            else {
                if (!(object instanceof Number)) {
                    continue;
                }
                result.add(((Number)object).intValue());
            }
        }
        return result;
    }
    
    public List<Boolean> getBooleanList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Boolean>(0);
        }
        final List<Boolean> result = new ArrayList<Boolean>();
        for (final Object object : list) {
            if (object instanceof Boolean) {
                result.add((Boolean)object);
            }
            else {
                if (!(object instanceof String)) {
                    continue;
                }
                if (Boolean.TRUE.toString().equals(object)) {
                    result.add(true);
                }
                else {
                    if (!Boolean.FALSE.toString().equals(object)) {
                        continue;
                    }
                    result.add(false);
                }
            }
        }
        return result;
    }
    
    public List<Double> getDoubleList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Double>(0);
        }
        final List<Double> result = new ArrayList<Double>();
        for (final Object object : list) {
            if (object instanceof Double) {
                result.add((Double)object);
            }
            else if (object instanceof String) {
                try {
                    result.add(Double.valueOf((String)object));
                }
                catch (Exception ex) {}
            }
            else if (object instanceof Character) {
                result.add((double)(char)object);
            }
            else {
                if (!(object instanceof Number)) {
                    continue;
                }
                result.add(((Number)object).doubleValue());
            }
        }
        return result;
    }
    
    public List<Float> getFloatList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Float>(0);
        }
        final List<Float> result = new ArrayList<Float>();
        for (final Object object : list) {
            if (object instanceof Float) {
                result.add((Float)object);
            }
            else if (object instanceof String) {
                try {
                    result.add(Float.valueOf((String)object));
                }
                catch (Exception ex) {}
            }
            else if (object instanceof Character) {
                result.add((float)(char)object);
            }
            else {
                if (!(object instanceof Number)) {
                    continue;
                }
                result.add(((Number)object).floatValue());
            }
        }
        return result;
    }
    
    public List<Long> getLongList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Long>(0);
        }
        final List<Long> result = new ArrayList<Long>();
        for (final Object object : list) {
            if (object instanceof Long) {
                result.add((Long)object);
            }
            else if (object instanceof String) {
                try {
                    result.add(Long.valueOf((String)object));
                }
                catch (Exception ex) {}
            }
            else if (object instanceof Character) {
                result.add((long)(char)object);
            }
            else {
                if (!(object instanceof Number)) {
                    continue;
                }
                result.add(((Number)object).longValue());
            }
        }
        return result;
    }
    
    public List<Byte> getByteList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Byte>(0);
        }
        final List<Byte> result = new ArrayList<Byte>();
        for (final Object object : list) {
            if (object instanceof Byte) {
                result.add((Byte)object);
            }
            else if (object instanceof String) {
                try {
                    result.add(Byte.valueOf((String)object));
                }
                catch (Exception ex) {}
            }
            else if (object instanceof Character) {
                result.add((byte)(char)object);
            }
            else {
                if (!(object instanceof Number)) {
                    continue;
                }
                result.add(((Number)object).byteValue());
            }
        }
        return result;
    }
    
    public List<Character> getCharacterList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Character>(0);
        }
        final List<Character> result = new ArrayList<Character>();
        for (final Object object : list) {
            if (object instanceof Character) {
                result.add((Character)object);
            }
            else if (object instanceof String) {
                final String str = (String)object;
                if (str.length() != 1) {
                    continue;
                }
                result.add(str.charAt(0));
            }
            else {
                if (!(object instanceof Number)) {
                    continue;
                }
                result.add((char)((Number)object).intValue());
            }
        }
        return result;
    }
    
    public List<Short> getShortList(final String key) {
        final List<?> list = (List<?>)this.getList(key);
        if (list == null) {
            return new ArrayList<Short>(0);
        }
        final List<Short> result = new ArrayList<Short>();
        for (final Object object : list) {
            if (object instanceof Short) {
                result.add((Short)object);
            }
            else if (object instanceof String) {
                try {
                    result.add(Short.valueOf((String)object));
                }
                catch (Exception ex) {}
            }
            else if (object instanceof Character) {
                result.add((short)(char)object);
            }
            else {
                if (!(object instanceof Number)) {
                    continue;
                }
                result.add(((Number)object).shortValue());
            }
        }
        return result;
    }
    
    public List<Map> getMapList(final String key) {
        final List<Map> list = (List<Map>)this.getList(key);
        final List<Map> result = new ArrayList<Map>();
        if (list == null) {
            return result;
        }
        for (final Object object : list) {
            if (object instanceof Map) {
                result.add((Map)object);
            }
        }
        return result;
    }
    
    public boolean exists(String key, final boolean ignoreCase) {
        if (ignoreCase) {
            key = key.toLowerCase();
        }
        for (String existKey : this.getKeys(true)) {
            if (ignoreCase) {
                existKey = existKey.toLowerCase();
            }
            if (existKey.equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean exists(final String key) {
        return this.exists(key, false);
    }
    
    public void remove(final String key) {
        if (key == null || key.isEmpty()) {
            return;
        }
        if (super.containsKey(key)) {
            super.remove(key);
        }
        else if (this.containsKey(".")) {
            final String[] keys = key.split("\\.", 2);
            if (super.get(keys[0]) instanceof ConfigSection) {
                final ConfigSection section = (ConfigSection) super.get(keys[0]);
                section.remove(keys[1]);
            }
        }
    }
    
    public Set<String> getKeys(final boolean child) {
        final Set<String> keys = new LinkedHashSet<String>();
        final Set<String> set = null;
        this.entrySet().forEach(entry -> {
            set.add(entry.getKey());
            if (entry.getValue() instanceof ConfigSection && child) {
                ((ConfigSection)entry.getValue()).getKeys(true).forEach(childKey -> set.add(entry.getKey() + "." + childKey));
            }
            return;
        });
        return keys;
    }
    
    public Set<String> getKeys() {
        return this.getKeys(true);
    }
}

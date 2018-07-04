package network.minter.blockchainapi.repo;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
class QueryObjectConverter {
    private final Gson mGson;
    private String mDateFormat = "yyyy-MM-dd";

    public QueryObjectConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .setDateFormat(mDateFormat);

        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        mGson = gsonBuilder.create();
    }

    public void setDateFormat(String dateFormat) {
        mDateFormat = dateFormat;
    }

    /**
     * Converts an object to serialized URI parameters.
     *
     * @param value The value to be converted (can be an object or collection).
     * @return Serialized URI parameters for use with {@literal @}{@link
     * retrofit2.http.QueryMap}(encoded=true).
     */
    public Map<String, String> convert(Object value) {
        return convertTree(mGson.toJsonTree(value));
    }

    /**
     * Converts an object to serialized URI parameters.
     *
     * @param value The value to be converted (can be an object or collection).
     * @return Serialized URI parameters for use with {@literal @}{@link
     * retrofit2.http.QueryMap}(encoded=true).
     */
    public Map<String, JsonPrimitive> convertRaw(Object value) {
        return convertTreeRaw(mGson.toJsonTree(value));
    }


    /**
     * Converts the given JSON tree to serialized URI parameters. This is equivalent to
     * helpers.js/encodeParams.
     *
     * @param tree The JSON tree (can be an object or array).
     * @return Serialized URI parameters for use with {@literal @}{@link
     * retrofit2.http.QueryMap}(encoded=false).
     */
    public Map<String, String> convertTree(JsonElement tree) {
        ParamsMap<String> params = new ParamsMap<>();
        if (tree.isJsonArray()) {
            int i = 0;
            for (JsonElement element : tree.getAsJsonArray()) {
                buildObjectParams(Integer.toString(i), element, params);
                i++;
            }
        } else if (tree.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : tree.getAsJsonObject().entrySet()) {
                buildObjectParams(entry.getKey(), entry.getValue(), params);
            }
        } else if (!tree.isJsonNull()) {
            throw new IllegalArgumentException("Cannot convert " + tree.toString());
        }
        return params;
    }

    public Map<String, JsonPrimitive> convertTreeRaw(JsonElement tree) {
        ParamsMap<JsonPrimitive> params = new ParamsMap<>();
        if (tree.isJsonArray()) {
            int i = 0;
            for (JsonElement element : tree.getAsJsonArray()) {
                buildObjectParamsRaw(Integer.toString(i), element, params);
                i++;
            }
        } else if (tree.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : tree.getAsJsonObject().entrySet()) {
                buildObjectParamsRaw(entry.getKey(), entry.getValue(), params);
            }
        } else if (!tree.isJsonNull()) {
            throw new IllegalArgumentException("Cannot convert " + tree.toString());
        }
        return params;
    }

    /**
     * Recursive helper method for {@link #convertTreeRaw(JsonElement)}.
     *
     * @param prefix The prefix for the parameter names.
     * @param tree   The remaining JSON tree.
     * @param params The params object to write to.
     */
    private void buildObjectParamsRaw(String prefix, JsonElement tree, ParamsMap<JsonPrimitive> params) {
        if (tree.isJsonArray()) {
            int i = 0;
            for (JsonElement element : tree.getAsJsonArray()) {
                buildObjectParamsRaw(prefix + "[" + i + "]", element, params);
                i++;
            }
        } else if (tree.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : tree.getAsJsonObject().entrySet()) {
                buildObjectParamsRaw(prefix + "[" + entry.getKey() + "]", entry.getValue(), params);
            }
        } else if (tree.isJsonPrimitive()) {
            params.put(prefix, tree.getAsJsonPrimitive());
        }
    }

    /**
     * Recursive helper method for {@link #convertTree(JsonElement)}. This is equivalent to
     * helpers.js/buildObjectParams.
     *
     * @param prefix The prefix for the parameter names.
     * @param tree   The remaining JSON tree.
     * @param params The params object to write to.
     */
    private void buildObjectParams(String prefix, JsonElement tree, ParamsMap<String> params) {
        if (tree.isJsonArray()) {
            int i = 0;
            for (JsonElement element : tree.getAsJsonArray()) {
                buildObjectParams(prefix + "[" + i + "]", element, params);
                i++;
            }
        } else if (tree.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : tree.getAsJsonObject().entrySet()) {
                buildObjectParams(prefix + "[" + entry.getKey() + "]", entry.getValue(), params);
            }
        } else if (tree.isJsonPrimitive()) {
            params.put(prefix, tree.getAsJsonPrimitive().getAsString());
        }
    }

    /**
     * A map class that allows multiple entries per key.
     */
    private static class ParamsMap<Value> implements Map<String, Value> {

        private final Set<Entry<String, Value>> entries = new LinkedHashSet<>();

        @Override
        public int size() {
            return entries.size();
        }

        @Override
        public boolean isEmpty() {
            return entries.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            for (Entry<String, Value> entry : entries) {
                if (entry.getKey().equals(key)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            for (Entry<String, Value> entry : entries) {
                if (entry.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @param key The key to look for.
         * @return The value of the FIRST matching entry or null if none matches.
         */
        @Override
        public Value get(Object key) {
            for (Entry<String, Value> entry : entries) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
            return null;
        }

        @Override
        public Value put(String key, Value value) {
            entries.add(new ParamEntry<>(key, value));
            return null;
        }

        @Override
        public Value remove(Object key) {

            return null;
        }

        @Override
        public void putAll(@NonNull Map<? extends String, ? extends Value> m) {
            for (Entry<? extends String, ? extends Value> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void clear() {
            entries.clear();
        }

        @NonNull
        @Override
        public Set<String> keySet() {
            Set<String> kSet = new LinkedHashSet<>();
            for (Entry<String, Value> entry : entries) {
                kSet.add(entry.getKey());
            }
            return kSet;
        }

        @NonNull
        @Override
        public Collection<Value> values() {
            LinkedList<Value> vList = new LinkedList<>();
            for (Entry<String, Value> entry : entries) {
                vList.add(entry.getValue());
            }
            return vList;
        }

        @NonNull
        @Override
        public Set<Entry<String, Value>> entrySet() {
            return entries;
        }
    }

    private static class ParamEntry<Value> implements Map.Entry<String, Value> {
        private final String key;
        private final Value value;

        public ParamEntry(String key, Value value) {
            this.key = Objects.requireNonNull(key);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Value getValue() {
            return value;
        }

        @Override
        public Value setValue(Value value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParamEntry that = (ParamEntry) o;
            return key.equals(that.key) && value.equals(that.value);

        }
    }

}

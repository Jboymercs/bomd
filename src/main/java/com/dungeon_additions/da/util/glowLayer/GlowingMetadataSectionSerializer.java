package com.dungeon_additions.da.util.glowLayer;

import com.google.gson.*;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Tuple;

import java.lang.reflect.Type;

public class GlowingMetadataSectionSerializer extends BaseMetadataSectionSerializer<GlowingMetadataSection> implements JsonSerializer<GlowingMetadataSection> {
    @Override
    public String getSectionName() {
        return "glowsections";
    }

    @Override
    public GlowingMetadataSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonobject = JsonUtils.getJsonObject(json, "metadata section");
        if (jsonobject.has("sections")) {
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sections");
            GlowingMetadataSection result = new GlowingMetadataSection();
            for (int i = 0; i < jsonarray.size(); ++i) {
                JsonElement jsonelement = jsonarray.get(i);

                int x1 = JsonUtils.getInt(jsonelement.getAsJsonObject(), "x1", 0);
                int y1 = JsonUtils.getInt(jsonelement.getAsJsonObject(), "y1", 0);
                int x2 = JsonUtils.getInt(jsonelement.getAsJsonObject(), "x2", 0);
                int y2 = JsonUtils.getInt(jsonelement.getAsJsonObject(), "y2", 0);

                Tuple<Integer, Integer> pos1 = new Tuple<>(x1, y1);
                Tuple<Integer, Integer> pos2 = new Tuple<>(x2, y2);
                if (pos1.getFirst() <= pos2.getFirst() && pos1.getSecond() <= pos2.getSecond()) {
                    result.addSection(pos1, pos2);
                }

            }
            if (!result.isEmpty()) {
                return result;
            }
        }
        return null;
    }

    @Override
    public JsonElement serialize(GlowingMetadataSection src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonobject = new JsonObject();

        if (!src.isEmpty()) {
            JsonArray jsonarray = new JsonArray();

            for (Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> entry : src.getGlowingSections()) {
                JsonObject jsonobject1 = new JsonObject();
                jsonobject1.addProperty("x1", (entry.getFirst().getFirst()));
                jsonobject1.addProperty("y1", (entry.getFirst().getSecond()));
                jsonobject1.addProperty("x2", (entry.getSecond().getFirst()));
                jsonobject1.addProperty("y2", (entry.getSecond().getSecond()));

                jsonarray.add(jsonobject1);
            }

            jsonobject.add("sections", jsonarray);
        }

        return jsonobject;
    }
}

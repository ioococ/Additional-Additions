package dqu.additions.behaviour;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dqu.additions.Additions;
import dqu.additions.config.ConfigProperty;
import dqu.additions.config.value.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;

public class BehaviourManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Map<ResourceLocation, ConfigProperty> behaviours = ImmutableMap.of();
    public static BehaviourManager INSTANCE;
    public static int loads;

    public BehaviourManager() {
        super(GSON, "behaviour");
    }

    @Override
    public String getName() {
        return "behaviour";
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        HashMap<ResourceLocation, ConfigProperty> hashMap = new HashMap<>();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ResourceLocation resourceLocation = (ResourceLocation) entry.getKey();

            if (!resourceLocation.getNamespace().equals(Additions.namespace)) {
                continue;
            }

            JsonElement jsonElement = (JsonElement) entry.getValue();
            List<ConfigProperty> properties = loadBehaviour(resourceLocation, jsonElement);
            ListConfigValue listConfigValue = new ListConfigValue().putAll(properties);
            hashMap.put(resourceLocation, new ConfigProperty(resourceLocation.getPath(), listConfigValue));
        }

        this.behaviours = ImmutableMap.copyOf(hashMap);
        Additions.LOGGER.info("[{}] Loaded {} behaviours", Additions.namespace, this.behaviours.size());
        loads++;
    }

    private static List<ConfigProperty> loadBehaviour(ResourceLocation resourceLocation, JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        List<ConfigProperty> properties = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            BehaviourValues.getByName(key).ifPresentOrElse(behaviour -> {
                switch (behaviour.getType()) {
                    case STRING -> properties.add(new ConfigProperty(key, new StringConfigValue(value.getAsString())));
                    case BOOLEAN -> properties.add(new ConfigProperty(key, new BooleanConfigValue(value.getAsBoolean())));
                    case INTEGER -> properties.add(new ConfigProperty(key, new IntegerConfigValue(value.getAsInt())));
                    case FLOAT -> properties.add(new ConfigProperty(key, new FloatConfigValue(value.getAsFloat())));
                    default -> Additions.LOGGER.warn("[{}] Incorrect property: {} in behaviour {}. This shouldn't happen, please report.", Additions.namespace, key, resourceLocation.getPath());
                }
            }, () -> Additions.LOGGER.warn("[{}] Unknown property: {} in behaviour {}.", Additions.namespace, key, resourceLocation.getPath()));
        }

        return properties;
    }

    public ConfigProperty getBehaviour(String name) {
        return this.behaviours.get(new ResourceLocation(Additions.namespace, name));
    }

    public <T> T getBehaviourValue(String name, BehaviourValues values) {
        ConfigProperty property = getBehaviour(name);
        if (property == null) return null;
        ListConfigValue list = (ListConfigValue) property.value();
        String key = values.getName();
        if (list.get(key) == null || list.get(key).value() == null) {
            return null;
        } else {
            return (T) list.get(key).value().getValue();
        }
    }

    public Map<ResourceLocation, ConfigProperty> getBehaviours() {
        return this.behaviours;
    }

    static {
        INSTANCE = new BehaviourManager();
    }
}

package valandur.webapi.api.cache;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.DataManipulator;
import valandur.webapi.api.WebAPIAPI;
import valandur.webapi.api.serialize.ISerializeService;
import valandur.webapi.api.serialize.JsonDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CachedObject<T> implements ICachedObject<T> {
    protected long cachedAt;
    protected long cacheDuration = 0;

    protected ICacheService cacheService;
    protected ISerializeService serializeService;
    protected Class<? extends T> clazz;

    protected Map<String, Object> data = new HashMap<>();
    @Override
    @JsonDetails
    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }


    public CachedObject(T value) {
        this(value, true);
    }
    public CachedObject(T value, boolean serializeDataHolder) {
        this.cachedAt = System.nanoTime();
        this.cacheService = WebAPIAPI.getCacheService().orElse(null);
        this.serializeService = WebAPIAPI.getJsonService().orElse(null);

        this.cacheDuration = cacheService.getCacheDurationFor(this.getClass());

        if (value != null) this.clazz = (Class<? extends T>) value.getClass();

        if (serializeDataHolder && value instanceof DataHolder) {
            DataHolder holder = (DataHolder)value;

            Map<String, Class<? extends DataManipulator<?, ?>>> supData = serializeService.getSupportedData();
            for (Map.Entry<String, Class<? extends DataManipulator<?, ?>>> entry : supData.entrySet()) {
                try {
                    if (!holder.supports(entry.getValue()))
                        continue;

                    Optional<?> m = holder.get(entry.getValue());

                    if (!m.isPresent())
                        continue;

                    data.put(entry.getKey(), ((DataManipulator) m.get()).copy());
                } catch (IllegalArgumentException | IllegalStateException ignored) {
                }
            }
        }
    }

    @Override
    public String getLink() {
        return null;
    }

    @JsonIgnore
    @Override
    public Optional<T> getLive() {
        return Optional.empty();
    }

    @JsonIgnore
    @Override
    public final boolean isExpired() {
        return (System.nanoTime() - cachedAt) / 1000000000 > cacheDuration;
    }
}

package valandur.webapi.api.cache;

import org.spongepowered.api.data.DataHolder;

import java.util.Map;
import java.util.Optional;

/**
 * The base class for all cached objects.
 */
public interface ICachedObject<T> {

    /**
     * Gets any data associated with the object if the original object was a {@link DataHolder}.
     * @return Data stored on the object.
     */
    Map<String, Object> getData();

    /**
     * Gets a link representing a Web-API endpoint where details for the object can be retrieved..
     * @return The link to the details of the object, or null if not applicable.
     */
    String getLink();

    /**
     * Tries to get the live version this object is representing.
     * @return An optional containing the live version of this object if available, empty otherwise.
     */
    Optional<T> getLive();

    /**
     * Checks if this cached object has expired.
     * @return True if this cached object is considered expired, false otherwise.
     */
    boolean isExpired();
}

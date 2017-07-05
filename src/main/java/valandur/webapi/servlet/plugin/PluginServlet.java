package valandur.webapi.servlet.plugin;

import org.eclipse.jetty.http.HttpMethod;
import valandur.webapi.api.annotation.WebAPIEndpoint;
import valandur.webapi.api.annotation.WebAPIServlet;
import valandur.webapi.api.cache.plugin.ICachedPluginContainer;
import valandur.webapi.api.servlet.WebAPIBaseServlet;
import valandur.webapi.servlet.ServletData;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@WebAPIServlet(basePath = "plugin")
public class PluginServlet extends WebAPIBaseServlet {

    @WebAPIEndpoint(method = HttpMethod.GET, path = "/", perm = "list")
    public void getPlugins(ServletData data) {
        data.addJson("ok", true, false);
        data.addJson("plugins", cacheService.getPlugins(), data.getQueryParam("details").isPresent());
    }

    @WebAPIEndpoint(method = HttpMethod.GET, path = "/:plugin", perm = "one")
    public void getPlugin(ServletData data, String pluginName) {
        Optional<ICachedPluginContainer> plugin = cacheService.getPlugin(pluginName);
        if (!plugin.isPresent()) {
            data.sendError(HttpServletResponse.SC_NOT_FOUND, "Plugin with id '" + pluginName + "' could not be found");
            return;
        }

        data.addJson("ok", true, false);
        data.addJson("plugin", plugin.get(), true);
    }
}

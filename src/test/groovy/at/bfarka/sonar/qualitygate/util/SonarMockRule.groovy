package at.bfarka.sonar.qualitygate.util

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.junit.Rule
import org.junit.rules.ExternalResource

import static com.google.common.collect.ImmutableMap.builder

/**
 * Created by berndfarka on 23.11.14.
 */
class SonarMockRule extends ExternalResource {

    private static final int PORT = 8080


    private final Server server = new Server(PORT);
    private final MockServlet servlet = new MockServlet()


    SonarMockRule() {
        // The ServletHandler is a dead simple way to create a context handler that is backed by an instance of a
        // Servlet.  This handler then needs to be registered with the Server object.
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        // Passing in the class for the servlet allows jetty to instantite an instance of that servlet and mount it
        // on a given context path.
        ServletHolder holder = new ServletHolder()
        holder.setServlet(servlet);
        // !! This is a raw Servlet, not a servlet that has been configured through a web.xml or anything like that !!
        handler.addServletWithMapping(holder, "/sonar/*");
    }

    final String baseUrl = "http://127.0.0.1:${PORT}/sonar"

    @Override
    protected void before() throws Throwable {
        server.start()
    }

    @Override
    protected void after() {
        server.stop()
        servlet.reset()
    }

    public void addMockRequest(def resource, def fileName) {
        def request = new MockRequest("/api/resources", builder()
                .put("metrics", "alert_status")
                .put("resource", resource)
                .put("format", "json")
                .build()
                , fileName)
        this.servlet.addRequest(request)
    }

    public void resetServer() {
        this.servlet.reset()
    }
}
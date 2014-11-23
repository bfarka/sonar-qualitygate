package at.bfarka.sonar.qualitygate.util

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.junit.Rule
import org.junit.rules.ExternalResource
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


/**
 * Created by berndfarka on 23.11.14.
 */
class SonarMockRule extends ExternalResource{

    private final Server server = new Server(8080);
    private final MockServlet servlet = new MockServlet()


    SonarMockRule(){
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

    @Override
    protected void before() throws Throwable {
        server.start()
    }

    @Override
    protected void after() {
        server.stop()
    }

    public void addMockRequest(MockRequest request){
        this.servlet.addRequest(request)
    }
}
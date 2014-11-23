package at.bfarka.sonar.qualitygate.util;

import com.google.common.io.ByteStreams;
import org.apache.tools.ant.taskdefs.Classloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by berndfarka on 23.11.14.
 */
public class MockRequest {

    private final String pathInfo;
    private final Map<String, String> parameter;
    private final InputStream content;


    public MockRequest(String pathInfo, Map<String, String> parameter, String content) {
        this.pathInfo = pathInfo;
        this.parameter = parameter;
        this.content = ClassLoader.getSystemResourceAsStream(content);
    }

    public boolean matches(HttpServletRequest request) {
        if (request.getPathInfo().equals(pathInfo)) {
            if (request.getParameterMap().size() == parameter.size()) {
                boolean ret = true;
                for (Map.Entry<String, String> entry : parameter.entrySet()) {
                    ret = ret && parameter.get(entry.getKey()).equals(entry.getValue().toString());
                }
                return ret;
            }
        }

        return false;
    }

    public void createResponse(HttpServletResponse response) throws IOException{
        ByteStreams.copy(content, response.getOutputStream());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

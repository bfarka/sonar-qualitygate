package at.bfarka.sonar.qualitygate.util

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by berndfarka on 23.11.14.
 */
class MockServlet extends HttpServlet {


    List<MockRequest> requests = new LinkedList<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requests.each {
            if(it.matches(req)){
                it.createResponse(resp)
                return;

        }}

    }

    public addRequest(MockRequest request){
        this.requests.add(request);
    }
}

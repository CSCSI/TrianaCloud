package org.trianacode.TrianaCloud.Broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TrianaCloudServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: keyz
 * Date: 28/02/12
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class Results extends TrianaCloudServlet {
    public static ConcurrentHashMap<String, Task> resultMap;

    private String r_replyQueue;
    private String r_exchange;

    private Connection connection;
    private Channel channel;
    private ConnectionFactory factory;

    private class TaskReturn {
        public String name;
        public String origin;
        public String totalTime;
        public String dataType;
        public String data;
    }

    public void init() throws ServletException {
        try {
            resultMap = (ConcurrentHashMap<String, Task>) getServletContext().getAttribute("resultMap");
            if (resultMap == null) {
                throw new ServletException("Couldn't get resultMap");
            }
        } catch (NumberFormatException e) {
            throw new ServletException("No RabbitMQ password defined in init parameter. Cannot go on.");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pathInfo = isolatePath(request);
        String content = "";
        if (!pathInfo.equalsIgnoreCase("")) {
            write404Error(response, "Unknonw endpoint");
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            response.setStatus(200);
            response.setContentType("text/html");

            StringBuffer b = new StringBuffer();
            Iterator it = resultMap.entrySet().iterator();

            Set<TaskReturn> trs = new LinkedHashSet<TaskReturn>();

            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                Task t = (Task) pairs.getValue();
                if (t != null) {
                    TaskReturn r = new TaskReturn();
                    r.name = t.getName();
                    r.origin = t.getOrigin();
                    r.dataType = t.getReturnDataType();
                    r.totalTime = (t.getTotalTime().getTime() + "ms");
                    if (r.dataType.equalsIgnoreCase("string")) {
                        r.data = new String(t.getReturnData(), "UTF-8");
                    }
                    //b.append(mapper.writeValueAsString(t.getReturnData()));
                    trs.add(r);
                }
            }

            b.append(mapper.writeValueAsString(trs));

            System.out.println("JSON: " + b.toString());
            response.getWriter().println(b.toString());

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            StringBuffer stack = new StringBuffer("Error: " + e.getMessage() + "<br/>");
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement element : trace) {
                stack.append(element.toString()).append("<br/>");
            }
            writeError(response, 500, stack.toString());
        } catch (Throwable t) {
            writeThrowable(response, t);
        }
    }
}

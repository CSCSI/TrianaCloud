package org.trianacode.TrianaCloud.Broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TrianaCloudServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
        public String key;
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
        String name = "action";
        String value = request.getParameter(name);
        if (value == null) {
            // The request parameter 'param' was not present in the query string
            // e.g. http://hostname.com?a=b
        } else if ("".equals(value)) {
            // The request parameter 'param' was present in the query string but has no value
            // e.g. http://hostname.com?param=&a=b
        }

        String pathInfo = isolatePath(request);
        String content = "";
        if (!pathInfo.equalsIgnoreCase("")) {
            write404Error(response, "Unknonw endpoint");
            return;
        }

        if (value.equalsIgnoreCase("json")) {
            getJson(request, response);
        } else if (value.equalsIgnoreCase("file")) {
            getFile(request, response);
        } else if (value.equalsIgnoreCase("results")) {
            getResults(request, response);
        }
    }

    public void getJson(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            response.setStatus(200);
            response.setContentType("text/html");
            response.getWriter().println(makeJSON());
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

    public String makeJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        StringBuffer b = new StringBuffer();
        Iterator it = resultMap.entrySet().iterator();

        Set<TaskReturn> trs = new LinkedHashSet<TaskReturn>();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String key = (String) pairs.getKey();
            Task t = (Task) pairs.getValue();

            if (t != null) {
                TaskReturn r = new TaskReturn();
                r.key = key;
                r.name = t.getName();
                r.origin = t.getOrigin();
                r.dataType = t.getReturnDataType();
                r.totalTime = (t.getTotalTime().getTime() + "ms");
                if (r.dataType.equalsIgnoreCase("string")) {
                    r.data = new String(t.getReturnData(), "UTF-8");
                }
                trs.add(r);
            }
        }

        b.append(mapper.writeValueAsString(trs));

        System.out.println("JSON: " + b.toString());
        return b.toString();
    }

    public void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletOutputStream op = response.getOutputStream();
        String pname = "key";
        String kvalue = request.getParameter(pname);

        if (kvalue == null) {
            System.out.println("Key is null");
        }
        if (kvalue.equalsIgnoreCase("")) {
            System.out.println("Key is blank");
        }

        Task t = resultMap.get(kvalue);
        if (t == null) {
            System.out.println("Task is null");
        }

        byte[] data = t.getReturnData();

        response.setContentType("application/octet-stream");
        response.setContentLength((int) data.length);
        response.setHeader("Content-Disposition", "attachment; filename=" + t.getFileName());

        op.write(data);

        op.flush();
        op.close();
    }

    public void getResults(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            response.setStatus(200);
            response.setContentType("text/html");

            StringBuffer b = new StringBuffer();
            Iterator it = resultMap.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String key = (String) pairs.getKey();
                Task t = (Task) pairs.getValue();

                if (t != null) {
                    b.append("<div class=\"Title\">");
                    b.append(t.getOrigin());
                    b.append(" ");
                    b.append(t.getTotalTime() + " ms");
                    b.append("</div>");
                    if (t.getReturnDataType().equalsIgnoreCase("string")) {
                        b.append("<div class=\"toggle\"><pre>");
                        b.append(new String(t.getReturnData(), "UTF-8").replace("\n", "<br>"));
                        b.append("</pre></div><p/>");
                    } else {
                        b.append("<a href=\"/Broker/results?action=file&key=");
                        b.append(key);
                        b.append("\" display=\"none\">");
                        b.append(t.getName());
                        b.append("</a><p/>");
                    }
                }
            }

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

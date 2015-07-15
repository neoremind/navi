package com.baidu.beidou.navi.it;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;

import com.baidu.beidou.navi.server.NaviRpcServlet;

/**
 * ClassName: StandaloneServer <br/>
 * Function: 利用jetty启动独立于运行容器的服务
 * 
 * @author Zhang Xu
 */
public class StandaloneServer {

    protected Server server;

    public void start(int port) {
        try {
            server = new Server(port);
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            ContextLoaderListener listener = new ContextLoaderListener();
            context.setInitParameter("contextConfigLocation", "classpath*:/applicationContext.xml");
            context.addEventListener(listener);
            server.setHandler(context);
            context.addServlet(new ServletHolder(new NaviRpcServlet()), "/service_api/*");
            server.start();
        } catch (Throwable t) {
            System.err.println("Server failed to start. " + t.getMessage());
            throw new RuntimeException(t);
        }
    }

    public void stop() {
        if (server != null) {
            try {
                server.stop();
                server = null;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    public static void main(String[] args) {
        StandaloneServer server = new StandaloneServer();
        server.start(8088);
    }

}

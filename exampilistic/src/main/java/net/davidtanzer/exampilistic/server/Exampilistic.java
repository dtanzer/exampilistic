package net.davidtanzer.exampilistic.server;

import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Exampilistic {
	public static void main(String[] args) throws Exception {
		Server server = new Server(21500);
		
		ServletHolder wicketServletHolder = new ServletHolder(new WicketServlet());
		wicketServletHolder.setInitParameter("applicationClassName", Application.class.getName());
		wicketServletHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		
		ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
		context.addServlet(wicketServletHolder, "/*");
		
		server.setHandler(context);
		server.start();
	}
}

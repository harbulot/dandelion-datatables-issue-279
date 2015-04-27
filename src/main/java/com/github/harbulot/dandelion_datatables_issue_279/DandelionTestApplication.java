/*
 * [The "BSD licence"]
 * Copyright (c) 2015 Bruno Harbulot
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder may be used to endorse or 
 * promote products derived from this software without specific prior 
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.harbulot.dandelion_datatables_issue_279;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import com.github.harbulot.dandelion_datatables_issue_279.spring.ConfigBean;

public class DandelionTestApplication {
    public static void main(String[] args) throws Exception {
        Server server = new Server();

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setOutputBufferSize(32768);

        ServerConnector connector = new ServerConnector(server,
                new HttpConnectionFactory(httpConfig));
        int port;
        try {
            port = Integer.parseInt(System.getProperty("port"));
        } catch (Exception e) {
            port = 9012;
        }
        connector.setPort(port);
        int idleTimeout;
        try {
            idleTimeout = Integer.parseInt(System.getProperty("idle_timeout"));
        } catch (Exception e) {
            idleTimeout = 30000;
        }
        connector.setIdleTimeout(idleTimeout);

        server.addConnector(connector);

        // Creating a Servlet based on the Spring MVC configuration.
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet
                .setContextClass(AnnotationConfigWebApplicationContext.class);
        dispatcherServlet.setContextConfigLocation(ConfigBean.class.getName());
        ServletHolder servletHolder = new ServletHolder(dispatcherServlet);

        // Security filter
        FilterHolder securityFilterHolder = new FilterHolder(
                DelegatingFilterProxy.class);
        securityFilterHolder.setName("springSecurityFilterChain");
        securityFilterHolder.setDisplayName("springSecurityFilterChain");

        // Adding Dandelion/DataTables servlet and filter.
        ServletHolder dandelionServletHolder = new ServletHolder(
                new DandelionServlet());
        FilterHolder dandelionFilterHolder = new FilterHolder(
                new DandelionFilter());

        // Adding the servlets and filters to this embedded Jetty
        // context.
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("");
        context.addServlet(servletHolder, "/*");
        context.addServlet(dandelionServletHolder, "/dandelion-assets/*");
        context.addFilter(dandelionFilterHolder, "/*", null);
        context.addFilter(securityFilterHolder, "/*", null);

        final AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
        wac.register(ConfigBean.class);
        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(
                wac);
        context.addEventListener(contextLoaderListener);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context });

        server.setHandler(contexts);

        server.start();
    }
}

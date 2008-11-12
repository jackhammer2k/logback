/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package chapter7;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.StatusPrinter;

public class SimpleMDC {
  static public void main(String[] args) throws Exception {
    // You can put values in the MDC at any time. Before anything else 
    // we put the first name
    MDC.put("first", "Dorothy");

    // configure via the configuration file "chapter7/simpleMDC.xml"
    // which ships with the examples
    configureViaXML_File();
    
    // For educational purposes, the same configuration can 
    // be accomplished programmatically.
    // 
    // programmaticConfiguration();
    
    Logger logger = LoggerFactory.getLogger(SimpleMDC.class);
    // We now put the last name
    MDC.put("last", "Parker");

    // The most beautiful two words in the English language according
    // to Dorothy Parker:
    logger.info("Check enclosed.");
    logger.debug("The most beautiful two words in English.");

    MDC.put("first", "Richard");
    MDC.put("last", "Nixon");
    logger.info("I am not a crook.");
    logger.info("Attributed to the former US president. 17 Nov 1973.");
  }

  static void programmaticConfiguration() {
    // Configure logback
    LoggerContext loggerContext = (LoggerContext) LoggerFactory
        .getILoggerFactory();
    loggerContext.reset();
    PatternLayout layout = new PatternLayout();
    layout.setContext(loggerContext);
    layout.setPattern("%X{first} %X{last} - %m%n");
    layout.start();
    ConsoleAppender<LoggingEvent> appender = new ConsoleAppender<LoggingEvent>();
    appender.setContext(loggerContext);
    appender.setLayout(layout);
    appender.start();
    // cast root logger to c.q.logback.classic.Logger so that we can attach
    // an appender to it
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
        .getLogger("root");
    root.addAppender(appender);
  }

  static void configureViaXML_File() {
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    try {
      JoranConfigurator configurator = new JoranConfigurator();
      configurator.setContext(lc);
      lc.stop();
      URL url = Loader.getResourceBySelfClassLoader("chapter7/simpleMDC.xml");
      configurator.doConfigure(url);
    } catch (JoranException je) {
      StatusPrinter.print(lc);
    }
  }

}

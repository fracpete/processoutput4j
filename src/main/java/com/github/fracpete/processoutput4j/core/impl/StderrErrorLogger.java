/*
 * StderrErrorLogger.java
 * Copyright (C) 2025 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.processoutput4j.core.impl;

import com.github.fracpete.processoutput4j.core.ErrorLogger;

import java.io.Serializable;

/**
 * Simply outputs the errors on stderr.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class StderrErrorLogger
  implements Serializable, ErrorLogger {

  /**
   * Logs the message.
   *
   * @param msg		the message to output
   */
  @Override
  public void logError(String msg) {
    System.err.println(msg);
  }

  /**
   * Logs the message and throwable.
   *
   * @param msg		the message to output
   * @param t 		the exception
   */
  public void logError(String msg, Throwable t) {
    System.err.println(msg);
    t.printStackTrace();
  }
}

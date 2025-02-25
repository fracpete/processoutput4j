/*
 * ErrorLogger.java
 * Copyright (C) 2025 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.processoutput4j.core;

/**
 * Interface for error logging hooks.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface ErrorLogger {

  /**
   * Logs the message.
   *
   * @param msg		the message to output
   */
  public void logError(String msg);

  /**
   * Logs the message and throwable.
   *
   * @param msg		the message to output
   * @param t 		the exception
   */
  public void logError(String msg, Throwable t);
}

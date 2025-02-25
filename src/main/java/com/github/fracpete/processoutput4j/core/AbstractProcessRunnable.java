/*
 * AbstractProcessRunnable.java
 * Copyright (C) 2019-2025 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.core;

import com.github.fracpete.processoutput4j.core.impl.StderrErrorLogger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Ancestor for runnables that use a {@link Process} object.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractProcessRunnable
  implements Runnable {

  /** the polling timeout interval in milliseconds. */
  protected int m_PollInterval;

  /** the error logger. */
  protected ErrorLogger m_ErrorLogger;

  /** the blocking queue for the process. */
  protected BlockingQueue<Process> m_Queue;

  /** the process to read from. */
  protected Process m_Process;

  /** whether the runnable is still active. */
  protected boolean m_Running;

  /** whether the reader got stopped. */
  protected boolean m_Stopped;

  /**
   * Initializes the runnable.
   */
  public AbstractProcessRunnable() {
    this(1000, new StderrErrorLogger());
  }

  /**
   * Initializes the runnable.
   *
   * @param pollInterval 	the timeout interval in milliseconds to use when polling for new output
   * @param errorLogger 	the error logger to use
   */
  public AbstractProcessRunnable(int pollInterval, ErrorLogger errorLogger) {
    m_PollInterval = pollInterval;
    m_ErrorLogger  = errorLogger;
    m_Queue        = new ArrayBlockingQueue<>(1);
    m_Process      = null;
    m_Running      = false;
    m_Stopped      = false;
  }

  /**
   * Stops the execution.
   */
  public void stopExecution() {
    m_Stopped = true;
  }

  /**
   * Returns whether execution got stopped.
   *
   * @return		true if stopped
   */
  public boolean isStopped() {
    return m_Stopped;
  }

  /**
   * Returns whether the runnable is still active.
   *
   * @return		true if active
   */
  public boolean isRunning() {
    return m_Running;
  }

  /**
   * Sets the process to monitor.
   *
   * @param value	the process object
   */
  public void setProcess(Process value) {
    m_Queue.add(value);
  }

  /**
   * Returns the underlying {@link Process} object.
   *
   * @return		the process object, null if none set
   */
  public Process getProcess() {
    return m_Process;
  }

  /**
   * Just outputs the message on stderr.
   *
   * @param msg		the message to output
   */
  protected void logError(String msg) {
    m_ErrorLogger.logError(msg);
  }

  /**
   * Just outputs message and throwable on stderr.
   *
   * @param msg		the message to output
   * @param t 		the exception
   */
  protected void logError(String msg, Throwable t) {
    m_ErrorLogger.logError(msg, t);
  }

  /**
   * The actual processing loop.
   */
  protected abstract void doRun();

  /**
   * Reads the data from the process.
   */
  @Override
  public void run() {
    m_Running = true;

    try {
      while ((m_Process == null) && !isStopped()) {
        try {
	  m_Process = m_Queue.poll(m_PollInterval, TimeUnit.MILLISECONDS);
	}
	catch (Exception e) {
          // ignored
	}
      }

      if (isStopped())
        return;

      doRun();
    }
    catch (Exception e) {
      logError("Failed to process #" + m_Process.hashCode() + ":", e);
    }

    m_Running = false;
  }
}

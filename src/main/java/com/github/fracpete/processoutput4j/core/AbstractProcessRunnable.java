/*
 * AbstractProcessRunnable.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.core;

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

  /** the blocking queue for the process. */
  protected BlockingQueue<Process> m_Queue;

  /** the process to read from. */
  protected Process m_Process;

  /** whether the reader got stopped. */
  protected boolean m_Stopped;

  /**
   * Initializes the reader.
   */
  public AbstractProcessRunnable() {
    m_Queue   = new ArrayBlockingQueue<>(1);
    m_Process = null;
    m_Stopped = false;
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
    System.err.println(msg);
  }

  /**
   * Just outputs message and throwable on stderr.
   *
   * @param msg		the message to output
   * @param t 		the exception
   */
  protected void logError(String msg, Throwable t) {
    System.err.println(msg);
    t.printStackTrace();
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
    try {
      while ((m_Process == null) && !isStopped()) {
        try {
	  m_Process = m_Queue.poll(1000, TimeUnit.MILLISECONDS);
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
  }
}

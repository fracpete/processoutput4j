/*
 * AbstractProcessOutput.java
 * Copyright (C) 2017-2024 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.output;

import com.github.fracpete.processoutput4j.core.AbstractProcessRunnable;
import com.github.fracpete.processoutput4j.core.EnvironmentUtils;
import com.github.fracpete.processoutput4j.reader.AbstractProcessReader;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Serializable;

/**
 * Ancestor for classes that give access to the output generated by a process.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractProcessOutput
  implements Serializable {

  /** for serialization. */
  private static final long serialVersionUID = 1902809285333524039L;

  /** the command. */
  protected String[] m_Command;

  /** the environment variables. */
  protected String[] m_Environment;

  /** the exit code. */
  protected int m_ExitCode;

  /** the process. */
  protected transient Process m_Process;

  /** the timeout for the process in seconds (ignored if < 1). */
  protected int m_TimeOut;

  /** whether the process has timed out. */
  protected boolean m_TimedOut;

  /** for monitoring stderr. */
  protected transient AbstractProcessReader m_ReaderStdErr;

  /** for monitoring stdout. */
  protected transient AbstractProcessReader m_ReaderStdOut;

  /** for monitoring timeouts. */
  protected transient AbstractProcessRunnable m_RunnableTimeout;

  /**
   * Starts the monitoring process.
   */
  public AbstractProcessOutput() {
    initialize();
  }

  /**
   * For initializing the members.
   */
  protected void initialize() {
    m_Command     = new String[0];
    m_Environment = null;
    m_ExitCode    = 0;
    m_Process     = null;
    m_TimeOut     = -1;
    m_TimedOut    = false;
  }

  /**
   * Sets the timeout for the process in seconds.
   *
   * @param value	the timeout (less than 1 for no timeout)
   */
  public void setTimeOut(int value) {
    if (value < 1)
      value = -1;
    m_TimeOut = value;
  }

  /**
   * Returns the timeout for the process in seconds.
   *
   * @return		the timeout (less than 1 for no timeout)
   */
  public int getTimeOut() {
    return m_TimeOut;
  }

  /**
   * Performs the actual process monitoring.
   *
   * @param builder 	the process builder to monitor
   * @throws Exception	if writing to stdin fails
   */
  public void monitor(ProcessBuilder builder) throws Exception {
    monitor(null, builder);
  }

  /**
   * Performs the actual process monitoring.
   *
   * @param builder 	the process builder to monitor
   * @throws Exception	if writing to stdin fails
   */
  public void monitor(String input, ProcessBuilder builder) throws Exception {
    m_Command     = builder.command().toArray(new String[0]);
    m_Environment = EnvironmentUtils.envMapToArray(builder.environment());
    m_TimedOut    = false;

    // stderr
    m_ReaderStdErr = configureStdErr();
    Thread threade = new Thread(m_ReaderStdErr);
    threade.start();

    // stdout
    m_ReaderStdOut = configureStdOut();
    Thread threado = new Thread(m_ReaderStdOut);
    threado.start();

    // time out check
    m_RunnableTimeout = null;
    if (m_TimeOut > 0) {
      m_RunnableTimeout = configureTimeOutMonitor();
      Thread threadt = new Thread(m_RunnableTimeout);
      threadt.start();
    }

    m_Process = builder.start();
    m_ReaderStdErr.setProcess(m_Process);
    m_ReaderStdOut.setProcess(m_Process);
    if (m_RunnableTimeout != null)
      m_RunnableTimeout.setProcess(m_Process);

    // writing the input to the standard input of the process
    if (input != null) {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
	m_Process.getOutputStream()));
      writer.write(input);
      writer.close();
    }

    m_ExitCode = m_Process.waitFor();

    // wait for threads to finish
    while (threade.isAlive() || threado.isAlive()) {
      try {
	synchronized (this) {
	  wait(100);
	}
	if (m_TimedOut)
	  break;
      }
      catch (Exception e) {
	// ignored
      }
    }

    if (m_TimedOut) {
      if (threade.isAlive())
	threade.stop();
      if (threado.isAlive())
	threado.stop();
    }

    flush();

    m_Process = null;
  }

  /**
   * Performs the actual process monitoring.
   *
   * @param cmd		the command that was used
   * @param env		the environment
   * @param process 	the process to monitor
   * @throws Exception	if writing to stdin fails
   */
  public void monitor(String cmd, String[] env, Process process) throws Exception {
    monitor(cmd, env, null, process);
  }

  /**
   * Performs the actual process monitoring.
   *
   * @param cmd		the command that was used
   * @param env		the environment
   * @param input	the input to be written to the process via stdin, ignored if null
   * @param process 	the process to monitor
   * @throws Exception	if writing to stdin fails
   */
  public void monitor(String cmd, String[] env, String input, Process process) throws Exception {
    monitor(new String[]{cmd}, env, input, process);
  }

  /**
   * Performs the actual process monitoring.
   *
   * @param cmd		the command that was used
   * @param env		the environment
   * @param input	the input to be written to the process via stdin, ignored if null
   * @param process 	the process to monitor
   * @throws Exception	if writing to stdin fails
   */
  public void monitor(String[] cmd, String[] env, String input, Process process) throws Exception {
    m_Command     = cmd;
    m_Environment = env;
    m_Process     = process;
    m_TimedOut    = false;

    // stderr
    m_ReaderStdErr = configureStdErr();
    Thread threade = new Thread(m_ReaderStdErr);
    threade.start();
    m_ReaderStdErr.setProcess(m_Process);

    // stdout
    m_ReaderStdOut = configureStdOut();
    Thread threado = new Thread(m_ReaderStdOut);
    threado.start();
    m_ReaderStdOut.setProcess(m_Process);

    // time out check
    m_RunnableTimeout = null;
    if (m_TimeOut > 0) {
      m_RunnableTimeout = configureTimeOutMonitor();
      Thread threadt = new Thread(m_RunnableTimeout);
      threadt.start();
      m_RunnableTimeout.setProcess(m_Process);
    }

    // writing the input to the standard input of the process
    if (input != null) {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
	m_Process.getOutputStream()));
      writer.write(input);
      writer.close();
    }

    m_ExitCode = m_Process.waitFor();

    // wait for threads to finish
    while (threade.isAlive() || threado.isAlive()) {
      try {
	synchronized (this) {
	  wait(100);
	}
	if (m_TimedOut)
	  break;
      }
      catch (Exception e) {
	// ignored
      }
    }

    if (m_TimedOut) {
      if (threade.isAlive())
	threade.stop();
      if (threado.isAlive())
	threado.stop();
    }

    flush();

    m_Process         = null;
    m_ReaderStdErr    = null;
    m_ReaderStdOut    = null;
    m_RunnableTimeout = null;
  }

  /**
   * Configures the reader for stderr.
   *
   * @return		the configured reader
   */
  protected abstract AbstractProcessReader configureStdErr();

  /**
   * Configures the reader for stdout.
   *
   * @return		the configured reader
   */
  protected abstract AbstractProcessReader configureStdOut();

  /**
   * Configures the runnable for watching for time outs.
   *
   * @return		the configured runnable
   */
  protected AbstractProcessRunnable configureTimeOutMonitor() {
    return new AbstractProcessRunnable() {
      @Override
      protected void doRun() {
      long start = System.currentTimeMillis();
	while (m_Process.isAlive()) {
	  try {
	    synchronized (this) {
	      wait(500);
	    }
	    // time out defined and reached?
	    if (m_Process.isAlive()) {
	      if (((System.currentTimeMillis() - start) / 1000) >= m_TimeOut) {
		m_TimedOut = true;
		logError("Timeout of " + m_TimeOut + " seconds reached, terminating process...");
		m_Process.destroy();
		break;
	      }
	    }
	  }
	  catch (Exception e) {
	    // ignored
	  }
	}
      }
    };
  }

  /**
   * Returns the command that was used for the process.
   *
   * @return the command
   */
  public String[] getCommand() {
    return m_Command;
  }

  /**
   * Returns the environment.
   *
   * @return the environment, null if process inherited current one
   */
  public String[] getEnvironment() {
    return m_Environment;
  }

  /**
   * Returns whether the process has succeeded.
   *
   * @return true if succeeded, i.e., exit code = 0 and not timedout
   * @see #hasTimedOut()
   */
  public boolean hasSucceeded() {
    return (m_ExitCode == 0) && !hasTimedOut();
  }

  /**
   * Returns the exit code.
   *
   * @return the exit code
   */
  public int getExitCode() {
    return m_ExitCode;
  }

  /**
   * Returns whether the process timed out and got terminated.
   *
   * @return true if timedout
   * @see #getTimeOut()
   * @see #setTimeOut(int)
   */
  public boolean hasTimedOut() {
    return m_TimedOut;
  }

  /**
   * Returns the process.
   *
   * @return  the process, null if not available
   */
  public Process getProcess() {
    return m_Process;
  }

  /**
   * Flushes the readers.
   */
  public void flush() {
    if (m_ReaderStdErr != null)
      m_ReaderStdErr.flush();
    if (m_ReaderStdOut != null)
      m_ReaderStdOut.flush();
  }

  /**
   * Destroys the process if possible.
   */
  public void destroy() {
    if (m_Process != null)
      m_Process.destroy();
    if (m_ReaderStdErr != null)
      m_ReaderStdErr.stopExecution();
    if (m_ReaderStdOut != null)
      m_ReaderStdOut.stopExecution();
    if (m_RunnableTimeout != null)
      m_RunnableTimeout.stopExecution();
    flush();
  }

  /**
   * Returns a short description string.
   *
   * @return the description
   */
  @Override
  public String toString() {
    return "exit code=" + m_ExitCode;
  }
}

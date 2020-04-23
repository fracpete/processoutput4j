/*
 * PrefixedStreamingProcessOwner.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.core.impl;

import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;

/**
 * Simple class for streaming a process's stdout/stderr to this stdout,
 * but prefixing each line with the appropriate stdout/stderr prefix.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class PrefixedStreamingProcessOwner
  implements StreamingProcessOwner {

  /** default prefix for stdout. */
  public final static String DEFAULT_PREFIX_STDOUT = "[OUT] ";

  /** default prefix for stderr. */
  public final static String DEFAULT_PREFIX_STDERR = "[ERR] ";

  /** the prefix for stdout. */
  protected String m_PrefixStdOut;

  /** the prefix for stderr. */
  protected String m_PrefixStdErr;

  /**
   * Initializes the output using the default prefixes.
   *
   * @see 		#DEFAULT_PREFIX_STDOUT
   * @see		#DEFAULT_PREFIX_STDERR
   */
  public PrefixedStreamingProcessOwner() {
    this(DEFAULT_PREFIX_STDOUT, DEFAULT_PREFIX_STDERR);
  }

  /**
   * Initializes the output using the specified prefixes.
   *
   * @param prefixStdOut	the prefix for stdout
   * @param prefixStdErr	the prefix for stderr
   */
  public PrefixedStreamingProcessOwner(String prefixStdOut, String prefixStdErr) {
    m_PrefixStdOut = prefixStdOut;
    m_PrefixStdErr = prefixStdErr;
  }

  /**
   * Returns what output from the process to forward.
   *
   * @return 		StreamingProcessOutputType#BOTH
   */
  public StreamingProcessOutputType getOutputType() {
    return StreamingProcessOutputType.BOTH;
  }

  /**
   * Processes the incoming line.
   *
   * @param line	the line to process
   * @param stdout	whether stdout or stderr
   */
  public void processOutput(String line, boolean stdout) {
    System.out.println((stdout ? m_PrefixStdOut : m_PrefixStdErr) + line);
  }
}

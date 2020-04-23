/*
 * SimpleStreamingProcessOwner.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.core.impl;

import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;

/**
 * Simple class for streaming a process's stdout/stderr to this stdout/stderr.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SimpleStreamingProcessOwner
  implements StreamingProcessOwner {

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
    if (stdout)
      System.out.println(line);
    else
      System.err.println(line);
  }
}

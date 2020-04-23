/*
 * StreamingProcessOwner.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.core;

import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;

/**
 * Interface for classes that make use of {@link StreamingProcessOutput}.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface StreamingProcessOwner {

  /**
   * Returns what output from the process to forward.
   *
   * @return 		the output type
   */
  public StreamingProcessOutputType getOutputType();

  /**
   * Processes the incoming line.
   *
   * @param line	the line to process
   * @param stdout	whether stdout or stderr
   */
  public void processOutput(String line, boolean stdout);
}

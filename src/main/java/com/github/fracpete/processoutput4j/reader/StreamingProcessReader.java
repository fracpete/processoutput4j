/*
 * StreamingProcessReader.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.reader;

import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;

/**
 * Forwards the output from the process to the owning {@link StreamingProcessOwner}
 * object, if appropriate.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StreamingProcessReader
  extends AbstractProcessReader {

  /** the owner. */
  protected StreamingProcessOwner m_Owner;

  /** whether to forward the output to the owner. */
  protected boolean m_Forward;

  /**
   * Initializes the reader.
   *
   * @param owner the owning object
   * @param stdout  whether to read stdout or stderr
   */
  public StreamingProcessReader(StreamingProcessOwner owner, boolean stdout) {
    super(stdout);
    m_Owner = owner;
    m_Forward = (stdout && (m_Owner.getOutputType() == StreamingProcessOutputType.STDOUT))
	|| (!stdout && (m_Owner.getOutputType() == StreamingProcessOutputType.STDERR))
	|| (m_Owner.getOutputType() == StreamingProcessOutputType.BOTH);
  }

  /**
   * For processing the line read from stdout/stderr.
   *
   * @param line	the output line
   */
  @Override
  protected void process(String line) {
    if (m_Forward)
      m_Owner.processOutput(line, isStdout());
  }
}

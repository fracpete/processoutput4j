/*
 * CollectingProcessReader.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.reader;

/**
 * Reader for storing all content.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class CollectingProcessReader
  extends AbstractProcessReader {

  /** the string builder to store the data in. */
  protected StringBuilder m_Content;

  /**
   * Initializes the reader.
   *
   * @param stdout  	whether to read stdout or stderr
   * @param content	for storing the content
   */
  public CollectingProcessReader(boolean stdout, StringBuilder content) {
    super(stdout);
    m_Content = content;
  }

  /**
   * Returns the string builder for storing the content.
   *
   * @return		the string builder
   */
  public StringBuilder getContent() {
    return m_Content;
  }

  /**
   * For processing the line read from stdout/stderr.
   *
   * @param line	the output line
   */
  @Override
  protected void process(String line) {
    m_Content.append(line);
    m_Content.append('\n');
  }
}

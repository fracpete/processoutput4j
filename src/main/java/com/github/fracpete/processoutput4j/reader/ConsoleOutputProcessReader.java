/*
 * SimpleOutputProcessReader.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.reader;

/**
 * Just outputs the data to stdout/stderr.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class ConsoleOutputProcessReader
  extends AbstractProcessReader {

  /** the default prefix for stdout. */
  public static String PREFIX_STDOUT = "[OUT] ";

  /** the default prefix for stderr. */
  public static String PREFIX_STDERR = "[ERR] ";

  /** the prefix to use. */
  protected String m_Prefix;

  /**
   * Initializes the reader.
   *
   * @param stdout  whether to read stdout or stderr
   */
  public ConsoleOutputProcessReader(boolean stdout) {
    this(stdout, null);
  }

  /**
   * Initializes the reader.
   *
   * @param stdout  	whether to read stdout or stderr
   * @param prefix	the prefix to use, null for auto-prefix
   */
  public ConsoleOutputProcessReader(boolean stdout, String prefix) {
    super(stdout);
    m_Prefix = (prefix == null) ? (stdout ? PREFIX_STDOUT : PREFIX_STDERR) : prefix;
  }

  /**
   * Returns the prefix in use.
   *
   * @return		the prefix
   */
  public String getPrefix() {
    return m_Prefix;
  }

  /**
   * For processing the line read from stdout/stderr.
   *
   * @param line	the output line
   */
  @Override
  protected void process(String line) {
    if (m_Stdout)
      System.out.println(m_Prefix + line);
    else
      System.err.println(m_Prefix + line);
  }
}

/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * CollectingProcessOutput.java
 * Copyright (C) 2017-2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.output;

import com.github.fracpete.processoutput4j.reader.AbstractProcessReader;
import com.github.fracpete.processoutput4j.reader.CollectingProcessReader;

/**
 * Collects the process output (stdout and stderr) and makes them available
 * once the process finishes.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public final class CollectingProcessOutput
  extends AbstractProcessOutput {

  /** for serialization. */
  private static final long serialVersionUID = 1902809285333524039L;

  /** the stdout content. */
  protected StringBuilder m_StdOut;

  /** the stderr content. */
  protected StringBuilder m_StdErr;

  /**
   * For initializing the members.
   */
  @Override
  protected void initialize() {
    super.initialize();
    m_StdOut = new StringBuilder();
    m_StdErr = new StringBuilder();
  }

  /**
   * Configures the reader for stderr.
   *
   * @return		the configured reader
   */
  @Override
  protected AbstractProcessReader configureStdErr() {
    return new CollectingProcessReader(false, m_StdErr);
  }

  /**
   * Configures the reader for stdout.
   *
   * @param process 	the process to monitor
   * @return		the configured reader
   */
  @Override
  protected AbstractProcessReader configureStdOut() {
    return new CollectingProcessReader(true, m_StdOut);
  }

  /**
   * Returns the output on stdout.
   *
   * @return the output
   */
  public String getStdOut() {
    return m_StdOut.toString();
  }

  /**
   * Returns the output on stderr.
   *
   * @return the output
   */
  public String getStdErr() {
    return m_StdErr.toString();
  }
}

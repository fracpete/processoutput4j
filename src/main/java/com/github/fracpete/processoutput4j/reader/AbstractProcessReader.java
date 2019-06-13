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
 * AbstractProcessReader.java
 * Copyright (C) 2017-2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.reader;

import com.github.fracpete.processoutput4j.core.AbstractProcessRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ancestor for readers that read line from stdout/stderr of the provided
 * {@link Process} object.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractProcessReader
  extends AbstractProcessRunnable {

  /** whether to use stdout or stderr. */
  protected boolean m_Stdout;

  /**
   * Initializes the reader.
   *
   * @param stdout	whether to read stdout or stderr
   */
  public AbstractProcessReader(boolean stdout) {
    super();
    m_Stdout = stdout;
  }

  /**
   * Returns whether the reader is for stdout or stderr.
   *
   * @return		true if for stdout
   */
  public boolean isStdout() {
    return m_Stdout;
  }

  /**
   * For processing the line read from stdout/stderr.
   *
   * @param line	the output line
   */
  protected abstract void process(String line);

  /**
   * The actual processing loop.
   */
  protected void doRun() {
    String 		line;
    BufferedReader 	reader;

    try {
      if (m_Stdout)
	reader = new BufferedReader(new InputStreamReader(m_Process.getInputStream()), 1024);
      else
	reader = new BufferedReader(new InputStreamReader(m_Process.getErrorStream()), 1024);

      while (m_Process.isAlive()) {
        try {
          line = reader.readLine();
        }
        catch (IOException ioe) {
          // has process stopped?
          if (ioe.getMessage().toLowerCase().contains("stream closed"))
            return;
          else
            throw ioe;
        }
        if (line != null)
          process(line);
      }

      // make sure all data has been read
      while (true) {
        try {
          line = reader.readLine();
          if (line == null)
            break;
          else
	    process(line);
	}
	catch (Exception e) {
          break;
	}
      }
    }
    catch (Exception e) {
      System.err.println("Failed to read from " + (m_Stdout ? "stdout" : "stderr") + " for process #" + m_Process.hashCode() + ":");
      e.printStackTrace();
    }
  }
}

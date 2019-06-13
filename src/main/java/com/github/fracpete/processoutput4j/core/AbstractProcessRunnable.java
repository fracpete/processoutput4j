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
 * AbstractProcessRunnable.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
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
      System.err.println("Failed to process #" + m_Process.hashCode() + ":");
      e.printStackTrace();
    }
  }
}

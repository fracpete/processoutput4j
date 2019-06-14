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
 * ConsoleOutputProcessOutput.java
 * Copyright (C) 2017-2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.output;

import com.github.fracpete.processoutput4j.reader.AbstractProcessReader;
import com.github.fracpete.processoutput4j.reader.ConsoleOutputProcessReader;

import java.util.Arrays;

/**
 * A container class for the results obtained from executing a process.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public final class ConsoleOutputProcessOutput
  extends AbstractProcessOutput {

  /** for serialization. */
  private static final long serialVersionUID = 1902809285333524039L;

  /**
   * Configures the thread for stderr.
   *
   * @return		the configured thread, not yet started
   */
  protected AbstractProcessReader configureStdErr() {
    return new ConsoleOutputProcessReader(false, "");
  }

  /**
   * Configures the thread for stdout.
   *
   * @return		the configured thread, not yet started
   */
  @Override
  protected AbstractProcessReader configureStdOut() {
    return new ConsoleOutputProcessReader(true, "");
  }

  /**
   * Allows the execution of a command through this process output scheme.
   *
   * @param args	the command to launch
   * @throws Exception	if launching fails for some reason
   */
  public static void main(String[] args) throws Exception {
    ProcessBuilder 		builder;
    ConsoleOutputProcessOutput 	out;

    if (args.length == 0) {
      System.err.println("No command (+ options) provided!");
      System.exit(1);
    }

    builder = new ProcessBuilder();
    builder.command(args);
    out = new ConsoleOutputProcessOutput();
    out.monitor(builder);
    System.out.println();
    System.out.println("Command:");
    System.out.println(Arrays.asList(args));
    System.out.println("Exit code:");
    System.out.println(out.getExitCode());
  }
}

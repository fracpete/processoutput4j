/*
 * CollectingProcessOutput.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.output;

import com.github.fracpete.processoutput4j.reader.AbstractProcessReader;
import com.github.fracpete.processoutput4j.reader.CollectingProcessReader;

import java.util.Arrays;

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

  /**
   * Allows the execution of a command through this process output scheme.
   *
   * @param args	the command to launch
   * @throws Exception	if launching fails for some reason
   */
  public static void main(String[] args) throws Exception {
    ProcessBuilder 		builder;
    CollectingProcessOutput 	out;

    if (args.length == 0) {
      System.err.println("No command (+ options) provided!");
      System.exit(1);
    }

    builder = new ProcessBuilder();
    builder.command(args);
    out = new CollectingProcessOutput();
    out.monitor(builder);
    System.out.println();
    System.out.println("Command:");
    System.out.println(Arrays.asList(args));
    System.out.println("Exit code:");
    System.out.println(out.getExitCode());
    System.out.println("StdOut:");
    System.out.println(out.getStdOut());
    System.err.println("StdErr:");
    System.err.println(out.getStdErr());
  }
}

/*
 * StreamingProcessOutput.java
 * Copyright (C) 2017-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.output;

import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;
import com.github.fracpete.processoutput4j.core.impl.PrefixedStreamingProcessOwner;
import com.github.fracpete.processoutput4j.reader.AbstractProcessReader;
import com.github.fracpete.processoutput4j.reader.StreamingProcessReader;

import java.util.Arrays;

/**
 * Streams the data into the owning {@link StreamingProcessOwner} object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StreamingProcessOutput
  extends AbstractProcessOutput {

  private static final long serialVersionUID = 3891848111786764283L;

  /** the owner. */
  protected StreamingProcessOwner m_Owner;

  /**
   * Initializes the process output with the specified owning object.
   *
   * @param owner	the owning object
   */
  public StreamingProcessOutput(StreamingProcessOwner owner) {
    super();
    m_Owner = owner;
  }

  /**
   * Configures the thread for stderr.
   *
   * @return		the configured thread, not yet started
   */
  @Override
  protected AbstractProcessReader configureStdErr() {
    return new StreamingProcessReader(m_Owner, false);
  }

  /**
   * Configures the thread for stdout.
   *
   * @return		the configured thread, not yet started
   */
  @Override
  protected AbstractProcessReader configureStdOut() {
    return new StreamingProcessReader(m_Owner, true);
  }

  /**
   * Allows the execution of a command through this process output scheme.
   *
   * @param args	the command to launch
   * @throws Exception	if launching fails for some reason
   */
  public static void main(String[] args) throws Exception {
    ProcessBuilder 		builder;
    StreamingProcessOutput 	out;

    if (args.length == 0) {
      System.err.println("No command (+ options) provided!");
      System.exit(1);
    }

    builder = new ProcessBuilder();
    builder.command(args);
    out = new StreamingProcessOutput(new PrefixedStreamingProcessOwner());
    out.monitor(builder);
    System.out.println();
    System.out.println("Command:");
    System.out.println(Arrays.asList(args));
    System.out.println("Exit code:");
    System.out.println(out.getExitCode());
  }
}

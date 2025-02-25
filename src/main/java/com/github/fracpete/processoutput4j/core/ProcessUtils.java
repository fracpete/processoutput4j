/*
 * ProcessUtils.java
 * Copyright (C) 2025 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.processoutput4j.core;

/**
 * Helper class for processes.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class ProcessUtils {

  /** whether the OS is Windows. */
  protected static Boolean m_IsWindows;

  /** whether the OS is Linux. */
  protected static Boolean m_IsLinux;

  /** whether the OS is Android. */
  protected static Boolean m_IsAndroid;

  /**
   * Checks whether the operating system is Windows.
   *
   * @return		true if the OS is Windows flavor
   */
  public static synchronized boolean isWindows() {
    if (m_IsWindows == null)
      m_IsWindows = System.getProperty("os.name").toLowerCase().contains("windows");

    return m_IsWindows;
  }

  /**
   * Checks whether the operating system is Android.
   *
   * @return		true if the OS is Android flavor
   */
  public synchronized static boolean isAndroid() {
    if (m_IsAndroid == null) {
      m_IsAndroid = System.getProperty("java.vm.vendor").toLowerCase().contains("android")
		      || System.getProperty("java.vendor").toLowerCase().contains("android")
		      || System.getProperty("java.vendor.url").toLowerCase().contains("android");
    }

    return m_IsAndroid;
  }

  /**
   * Checks whether the operating system is Linux (but not Android).
   *
   * @return		true if the OS is Linux flavor (but not Android)
   */
  public synchronized static boolean isLinux() {
    if (m_IsLinux == null)
      m_IsLinux = System.getProperty("os.name").toLowerCase().startsWith("linux") && !isAndroid();

    return m_IsLinux;
  }

  /**
   * Destroys the process. Does not force the exit.
   *
   * @param process	the process to destroy
   */
  public static void destroy(Process process) {
    destroy(process, false);
  }

  /**
   * Destroys the process.
   *
   * @param process	the process to destroy
   * @param force 	whether to force the exit without giving the process a chance to clean up
   */
  public static void destroy(Process process, boolean force) {
    String[]	cmd;
    Process	kill;
    boolean	useFallback;
    int		exitCode;

    useFallback = true;
    cmd         = null;
    if (isLinux()) {
      if (force)
	cmd = new String[]{"kill", "-9", "" + process.pid()};
      else
	cmd = new String[]{"kill", "" + process.pid()};
    }
    else if (isWindows()) {
      if (force)
	cmd = new String[]{"taskkill", "/T", "/F", "/PID", "" + process.pid()};
      else
	cmd = new String[]{"taskkill", "/T", "/PID", "" + process.pid()};
    }

    if (cmd != null) {
      try {
	kill        = new ProcessBuilder().command(cmd).start();
	exitCode    = kill.waitFor();
	useFallback = (exitCode != 0);
      }
      catch (Exception e) {
	// ignored
      }
    }

    if (useFallback)
      process.destroyForcibly();
  }
}

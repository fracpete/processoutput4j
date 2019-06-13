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
 * EnvironmentUtils.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.processoutput4j.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for environment operations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class EnvironmentUtils {

  /**
   * Turns the environment map into array of key=value strings.
   *
   * @param env		the environment to convert
   * @return		the key=value strings
   */
  public static String[] envMapToArray(Map<String,String> env) {
    List<String> 	result;

    result = new ArrayList<>();

    if (env != null) {
      for (String key : env.keySet())
	result.add(key + "=" + env.get(key));
    }

    return result.toArray(new String[0]);
  }

  /**
   * Turns the environment strings (key=value) into a map.
   *
   * @param env		the key=value strings to convert
   * @return		the generated map
   */
  public static Map<String,String> envArrayToMap(String[] env) {
    Map<String,String>	result;
    int			index;

    result = new HashMap<>();

    if (env != null) {
      for (String e: env) {
        index = e.indexOf('=');
        if (index > -1)
          result.put(e.substring(0, index), e.substring(index + 1));
      }
    }

    return result;
  }
}

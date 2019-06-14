# processoutput4j
Simple Java library for capturing stdout/stderr of processes launched from within Java.

## Maven
Add the following artifact to your dependencies of your `pom.xml`:

```
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>processoutput4j</artifactId>
      <version>0.0.10</version>
    </dependency>
```

## Available schemes
The following schemes for capturing process output are available:
* `CollectingProcessOutput` - collects all the output and makes it available
  once the process has finished
* `ConsoleOutputProcessOutput` - simply outputs the process' output from
  stdout and stderr to the Java process' stdout and stderr as it occurrs
  rather than waiting till the process finishes.
* `StreamingProcessOutput` - requires an owner object that implements the
  `StreamingProcessOwner` interface, as it will receive the output collected
  from stdout/stderr for further processing in the owner.

## Stopping
The `AbstractProcessOutput` class offers the `destroy()` method, which
allows a currently running process to be killed off from another thread
(the `monitor` methods are waiting for the process to finish).

## Extending
Adding a new scheme for capturing the process output is quite simple. You
basically need to implement two classes:
* a *reader* class, derived from `AbstractProcessReader` that does something 
  with the data obtained from stdout and stderr of the process.
* an *output* class, derived from `AbstractProcessOutput` that instantiates
  the appropriate readers for stdout and stderr.

## Examples
The following executes the process and outputs any data from stdout/stderr
once the process has finished:
```java
import com.github.fracpete.processoutput4j.output.ConsoleOutputProcessOutput;
...
String[] cmd = new String[]{"/bin/ls", "-la", "/some/where"};
ProcessBuilder builder = new ProcessBuilder();
builder.command(cmd);
new ConsoleOutputProcessOutput().monitor(builder);
```

The following executes the process and outputs any data from stdout/stderr
once the process has finished:
```java
import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;
...
String[] cmd = new String[]{"/bin/ls", "-la", "/some/where"};
ProcessBuilder builder = new ProcessBuilder();
builder.command(cmd);
CollectingProcessOutput output = new CollectingProcessOutput();
output.monitor(builder);
System.out.println("exit code: " + output.getExitCode());
System.out.println(output.getStdOut());
```

The following executes the process and outputs any data from stdout/stderr
as it occurs, as well as setting a maximum execution time of 10 seconds:
```java
import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;
...
String[] cmd = new String[]{"/usr/bin/find", "/"};
ProcessBuilder builder = new ProcessBuilder();
builder.command(cmd);
ConsoleOutputProcessOutput output = new ConsoleOutputProcessOutput();
output.setTimeOut(10);
output.monitor(builder);
```
You can use the `hasTimedOut()` method to check whether the process timed out.

If you want to process the output (stdout/stderr) from the process
yourself, then you can use `StreamingProcessOutput` instead of 
`ConsoleOutputProcessOutput`. You only need to supply an object of a class
implementing the `StreamingProcessOwner` interface. Below is an example
that simply prefixes the output with either `[OUT]` or `[ERR]`: 

```java
import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;

public static class Output implements StreamingProcessOwner {
  public StreamingProcessOutputType getOutputType() {
    return StreamingProcessOutputType.BOTH;
  }
  public void processOutput(String line, boolean stdout) {
    System.out.println((stdout ? "[OUT] " : "[ERR] ") + line);
  }
}

...
String[] cmd = new String[]{"/usr/bin/find", "/etc"};
ProcessBuilder builder = new ProcessBuilder();
builder.command(cmd);
StreamingProcessOutput output = new StreamingProcessOutput(new Output());
output.monitor(builder);
```

Instead of implementing `StreamingProcessOwner` yourself, you can use one
of the following implementations:

* `com.github.fracpete.processoutput4j.core.impl.PrefixedStreamingProcessOwner`

  Forwards the stdout/stderr output of the monitored process to this one's
  stdout, using either default prefixes for stdout/stderr or user-supplied
  ones (like the above example).

* `com.github.fracpete.processoutput4j.core.impl.SimpleStreamingProcessOwner`

  Forwards the stdout/stderr output of the monitored process to this one's 
  stdout/stderr.

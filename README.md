# processoutput4j
Simple Java library for capturing stdout/stderr of processes launched from within Java.

## Maven
Add the following artifact to your dependencies of your `pom.xml`:

```
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>processoutput4j</artifactId>
      <version>0.0.2</version>
    </dependency>
```

## Available schemes
The following schemes for capturing process output are available:
* `CollectingProcessOutput` - collects all the output and makes it available
  once the process has finished
* `ConsoleOutputProcessOutput` - simply outputs the process' output from
  stdout and stderr to the Java process' stdout and stderr as it occurrs
  rather than waiting till the process finishes.

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

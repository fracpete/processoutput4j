# processoutput4j
Simple Java library for capturing stdout/stderr of processes launched from within Java.

## Maven
Add the following artifact to your dependencies of your `pom.xml`:

```
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>processoutput4j</artifactId>
      <version>0.0.1</version>
    </dependency>
```

## Examples
The following executes the process and outputs any data from stdout/stderr
once the process has finished:
```java
import com.github.fracpete.processoutput4j.output.ConsoleOutputProcessOutput;
...
String[] cmd = new String[]{"/bin/ls", "-la", "/some/where"};
ProcessBuilder builder = new ProcessBuilder();
builder.command(cmd);
new ConsoleOutputProcessOutput(builder);
```

The following executes the process and outputs any data from stdout/stderr
once the process has finished:
```java
import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;
...
String[] cmd = new String[]{"/bin/ls", "-la", "/some/where"};
ProcessBuilder builder = new ProcessBuilder();
builder.command(cmd);
CollectingProcessOutput output = new CollectingProcessOutput(builder);
System.out.println("exit code: " + output.getExitCode());
System.out.println(output.getStdOut());
```

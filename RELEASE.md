How to make a release
=====================

* Switch to Java 8

* Run the following command to deploy the artifact:

  ```
  mvn release:clean release:prepare release:perform
  ```

* Push all changes

* Publish on https://oss.sonatype.org/

* Update Maven artifact in [README.md](README.md#maven)
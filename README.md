# 2022-GHGUI

# Installing and Running

## Running With Maven

To simply run the application from a terminal, type:
```
mvn javafx:run
```

Note: If you do not have maven installed, you can replace `mvn` with `.\mvnw.cmd` on Windows or `./mvnw` on MacOS/Linux.

## Building a Custom Runtime (temporarily deprecated, [see here](/../../issues/66))

Since Java 9, the JRE has introduced the Java Platform Module System (JPMS). Jlink is a tool that allows us to compile our project alongside a custom runtime image that contains *only* the exact modules and runtime dependencies needed to create a functioning program. To create a custom runtime image, run:
```
mvn javafx:jlink
```
The output image will be located in `./target/ghgui/bin`, and can be run directly from the terminal.<br>
This runtime image can also be used to create an installer using jpackage with:
```
mvn jpackage:jpackage
```
This installer can be found at `./target/dist`, and will be native to whatever machine the command was run on. In the case of Windows, it will create a `.exe` or `.msi` file that should install ghgui when run. Building the installer may also depend on other programs being installed (Wix Toolset in the case of Windows)

## Compiling to native executable

Leveraging GraalVM's native-image technology, it is possible to compile Java ahead-of-time into a native executable (providing the code does not rely on reflection). Gluon have created a Maven plugin and a custom JVM that allows us to apply this to JavaFX applications. We can create this native image with:
```
mvn gluonfx:build
```
In order for this build to work, a recent build of [Gluon GraalVM must be installed](https://github.com/gluonhq/graal/releases), with the `GRAALVM_HOME` environment variable set. This build process also appears to conflict with the `_JAVA_OPTIONS` environment variable, so unsetting this variable is necessary.

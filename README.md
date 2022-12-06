# Overview
## Scope and Objectives
This project aims to create a simple, user experience oriented, cross-platform desktop app
that allows users to manage Git repositories, with focus on tight integration with GitHub. 

We hope that this project will offer a free to use open-source alternative to more popular 
applications such as GitHub Desktop and GitKraken.
## Features
When the project is finished, we aim to provide
- Full support for the typical Git workflow without the need to use a terminal
- A visual representation of branches and merges, and ways to act on the branch tree easily 
- Beginner (and rage-fuelled 3AM developer) friendly UI that makes it clear what will happen to the repository after every action
- UI to give you an idea of what is being worked on, and by whom, at a glance.
- Support for multiple repositories to be open at once, even for repositories owned by different GitHub accounts
- Integration for GitHub features, such as pull requests
- Support for customization of window sizes and proportions, as well as themes.
- Support for easily resolving conflicts
## End Users and Stakeholders 
This project is for everyone - at the end of it all, we would like to provide a release on GitHub for all to enjoy and modify to their liking, as well as some documentation to help them get started. 
As such, our stakeholders and end users are the developers that want a easier way to work with git. 

The Novice Collaborator will be able to use intuitive context menus that guide them through every common git operation without any unnecessary jargon. Contextual messages will tell them precisely what it is that they are doing and what the consequences would be, helping them to get used to the safe git workflow. This project is ideal for people looking to get into collaborative programming.

The Community Contributor will be able to manage multiple separate git repositories at once, with at-a-glance information clearly presented to them on every individual tab for the repositories. They could even sign in with separate GitHub accounts on separate tabs, and easily push to remotes and make pull requests with a simple and efficient interface.

The FOSS Fanatics will be able to modify the program to their liking - many aspects of the software will ideally be customizable, be it shortcuts or themes or default functionality, but anyone can just make a fork of GHGUI and change anything as they see fit, for any tweak that they would like.


## Ethics 
Our project will not be collecting or sending any user data aside from the data needed to interact with the 
GitHub API, and this connection will be strictly between GitHub and the user. Data may be created and stored locally as Git 
repository information or metadata.

# Installation and Use

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

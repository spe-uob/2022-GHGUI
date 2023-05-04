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

-----
## Building a Custom Runtime

Unfortunately due to many dependencies containing automatic modules, or not respecting the module system at all, this method is indefinitely deprecated.

<details>
<summary> DEPRECATED INSTRUCTIONS </summary>

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
</details>
<br />

-----
## Compiling to native executable

Unfortunately, this method has also had to be deprecated, for two main reasons:
- The sshd-sftp library from Apache throws lots of confusing errors when compiled with Graal. I've spent three days trying to figure out why and I'm about ready to bludgeon myself with a cup of coffee.
- Reflection in Graal is a very hard thing to configure, and since JavaFX is internally about 95% reflection... That's gonna be far more pain than it's worth.

<details>
<summary> DEPRECATED INSTRUCTIONS </summary>

Leveraging GraalVM's native-image technology, it is possible to compile Java ahead-of-time into a native executable (providing the code does not rely on reflection). Gluon have created a Maven plugin and a custom JVM that allows us to apply this to JavaFX applications. We can create this native image with:
```
mvn gluonfx:build
```
In order for this build to work, a recent build of [Gluon GraalVM must be installed](https://github.com/gluonhq/graal/releases), with the `GRAALVM_HOME` environment variable set. This build process also appears to conflict with the `_JAVA_OPTIONS` environment variable, so unsetting this variable is necessary.
</details>
<br />

-----
## Creating a fat jar (**EXTRA THICC**)

This method is strongly not recommended, and JavaFX goes so far as to crash if it detects that you're trying to do this.\
<br />
### **So let's do it anyways!**
<br />
Using the maven-shade-plugin, we can package our .jar file with all of the JavaFX runtime components needed to make this application work. To create the fat jar it's as simple as running:

```
mvn package
```

And the output should be located at `target/shade/ghgui.jar`\
Since we want to make this be cross-platform, it contains the GUI components for Windows, Linux and MacOS all bundled in here.

# Contributing

## Creating a PR

If you want to submit a PR, it's important that it's of good quality, so here's some guidelines:
- Give your PR a short, informative title so we can immediately see what you're submitting.
- Write a comment with your PR. Ideally you should explain both what you have done *and why.*
- Make sure your code passes the Checkstyle CI. "`TODO:`" comments are acceptable, but ideally nothing else should get flagged.
- Make sure your branch history is tidy. Ideally, it should follow these rules:
  - Always try to rebase instead of merging, as the branch history is much easier to follow this way
  - All files should end with LF, instead of CRLF, as git can spew unhelpful data if this changes
  - If a commit contains a change that doesn't make an impact (such as adding empty lines), these changes should not be included in the commit
  - All commits that introduce one or more files that quickly get deleted should be amended to no longer introduce those files
  - Conversely, any commit that removed a file introduced in an earlier commit should no longer remove that file
  - If a commit ends up empty in accordance with the rules above, delete the commit from the branch history
  - If a commit ends up with ~3 or fewer lines in accordance with the rules above, squash the commit

# Credits
The icon for this program is the Git Logo by Jason Long is licensed under the [Creative Commons Attribution 3.0 Unported License](https://creativecommons.org/licenses/by/3.0/).
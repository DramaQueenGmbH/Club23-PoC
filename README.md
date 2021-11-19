# Club23 PoC 

Prototype `SWT Browser` and Club23 integration

## Purpose

Solve technical challenges when communicating between a webpage embedded in an `SWT.Browser` widget and the hosting Java application.

## Running the app

Clone the project and open it in IntelliJ.
Navigate to the [App class](src/main/java/com/dramaqueen/club23/App.java).
Click the green Play button besides the `main()` method.

:warning: On macOS, this should likely fail on the first try.
But it created a Run-Configuration which you can now edit.

<img width="276" alt="image" src="https://user-images.githubusercontent.com/67784/142598181-40a06db3-ddbe-407b-b912-1d1a86808404.png">

Use the `Add VM Options` feature and add the following parameter:

`-XstartOnFirstThread`

<img width="507" alt="image" src="https://user-images.githubusercontent.com/67784/142598400-56410334-8e75-401a-ac57-31f2705d19c8.png">

By default, the app loads a webpage from its own resources into the `SWT Browser`.
It's also possible to load a custom URL.
The easiest way is to create another run configuration and pass the URL via program arguments:

<img width="736" alt="image" src="https://user-images.githubusercontent.com/67784/142638187-6f75704e-0542-4701-8275-9de793f31130.png">

## Repo layout

The webpage which is loaded into the `SWT.Browser` is located in [`index.html`](src/main/resources/com/dramaqueen/club23/ui/index.html).
It contains anchor tags with special links starting with `dq://`.
A `LocationListener` is registered on the `SWT.Browser` reacting on navigation events from these links.
A global [`FocusManager`](src/main/java/com/dramaqueen/club23/ui/FocusManager.java) is instructed to give keyboard focus to text fields with certain names contained in certain panels.

The values of [`Properties`](src/main/java/com/dramaqueen/club23/model/Property.java) from the Java [`Document`](src/main/java/com/dramaqueen/club23/model/Document.java) can also flow into the webpage.
This is achieved by registering a [`DocumentListener`](src/main/java/com/dramaqueen/club23/model/DocumentListener.java) and then executing a Javascript function defined in `index.html`.
This function tries to find a DOM element with an id matching the `Property` name.
The `.innerHTML` of the DOM element is then set to the value of the `Property`.

## Native image

This project supports building with [GraalVM](https://www.graalvm.org) [Native Image](https://www.graalvm.org/native-image/).
It uses the [Maven Native Image plugin](https://graalvm.github.io/native-build-tools/0.9.7.1/maven-plugin.html) in the POM profile `native`.
Follow the instructions for setting up GraalVM and Native Image on the site linked above.

Then you can build via:

```shell
mvn -Pnative -DskipTests package
```

The native executable is found at `target/club23-poc`.

### Native image compiler configuration

The native image compilation does not process the Java sources directly.
Instead, a regular JAR with JVM bytecode is produced first.
This JAR also contains resources.
The compiler options are passed via a [special set of resources](src/main/resources/META-INF/native-image/com.dramaqueen/club32-poc).
They control how the `.class` files and resources are processed.
Comprehensive information is available in this [Medium article](https://medium.com/graalvm/simplifying-native-image-generation-with-maven-plugin-and-embeddable-configuration-d5b283b92f57).
Normally, a reachability analysis would exclude any dead code.
But for many SWT classes, this would result in excluding important code.
This is where the reflection and JNI configuration comes into play and prevents that.
Also, many resources such as the native libraries contained in the SWT JAR are preserved and end up on the final native image.

:warning: The current native-image configuration only supports native macOS images.
The `pom.xml` currently specify the macOS version of SWT only. 

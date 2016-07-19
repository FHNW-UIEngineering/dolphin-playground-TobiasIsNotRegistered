#OpenDolphin Template#

This project provides a template for using [OpenDolphin](http://open-dolphin.org) 

- with plain Java 
- with a simple Servlet started through Jetty
- with JavaFX as the client technology
- with Gradle for build automation.

##Quick Setup##

Prerequisite: Java 8 or above.

Git clone to a location of you liking and you will see a gradle multi-project build.

It contains the following modules/projects
- client: in a remote scenario, this is the client. It typically contains only view classes.
- server: in a remote scenario, this is the server. It typically contains controller actions.
- shared: this one is totally optional. If used, it typically contains shared constants between client and server.
- combined: combines all the above in one JVM for starting with the in-memory configuration for develop/test/debug.

##Application introduction##

We implement a very simple application that contains only one label two text fields to show the various binding options
and two buttons to 'save' or 'reset' the value. 'Saving' will do nothing but printing the current field value
on the server side.

Both buttons are only enabled if there is really something to save/reset, i.e. the field value is dirty.
The dirty state is also visualized via a CSS class (background color changes).
Resetting triggers a 'shake' animation.

##Running the samples##

###JavaFx example###

Using Gradle you can call the following to start the application.

    ./gradlew run 

*todo: explain in-memory vs server mode and how to start each.*


When running the in server mode make sure that the server application is running too. To do so, call

    ./gradlew jettyRun

##More Info##

This has only been a first glance into the way that OpenDolphin operates.

Many more features are available and you may want to check out the
- user guide (http://open-dolphin.org/download/guide/index.html), the
- other demo sources (http://github.com/canoo/open-dolphin/tree/master/subprojects/demo-javafx), or
- the video demos (http://www.youtube.com/user/dierkkoenig).

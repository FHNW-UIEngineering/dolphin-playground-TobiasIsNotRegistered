#OpenDolphin Template

This project provides a template for using [OpenDolphin](http://open-dolphin.org) 

- with plain Java 
- with a simple Servlet started through Jetty
- with JavaFX as the client technology
- with Gradle for build automation.

##Quick Setup

Prerequisite: Java 8 or above.

Git clone to a location of your liking and you will see a gradle multi-project build.

It contains the following modules/projects
- client: in a remote scenario, this is the client. It typically contains only view classes.
- server: in a remote scenario, this is the server. It typically contains controller actions.
- shared: this one is totally optional. If used, it typically contains shared constants between client and server.
- combined: combines all the above in one JVM for starting with the in-memory configuration for develop/test/debug.

##Application introduction

We implement a very simple application that contains only a simple header section and an editor area
with three labels, two text fields to show the various binding options.
'Save' will do nothing but printing all modified attributes
on the server side. 'Reset' is used to reset all modified attributes to their initial value (or the value that has been saved).
'Next' will load a new 'Person' via a service call. 'German' and 'English' are for multi-language support.

'Save' and 'Reset' buttons are only enabled if there is really something to save/reset, i.e. at least one attribute value is dirty.
The dirty state is also visualized via a CSS class (background color changes).

The 'Name' field is marked as mandatory using a green border.

##Running the samples

###JavaFx example

Using Gradle you can call the following to start the application in **combined mode**, i.e. both server and client in a single JVM.

    ./gradlew run 


When running the application in **server mode** make sure that the server application is running too. To do so, call

    ./gradlew jettyRun
    
With a server running, you can start a client. Run from the client-module 

    myapp.MyRemoteStarter
    
##More Info

This has only been a first glance into the way that OpenDolphin operates.

Many more features are available and you may want to check out the
- user guide (http://open-dolphin.org/download/guide/index.html), the
- other demo sources (http://github.com/canoo/open-dolphin/tree/master/subprojects/demo-javafx), or
- the video demos (http://www.youtube.com/user/dierkkoenig).

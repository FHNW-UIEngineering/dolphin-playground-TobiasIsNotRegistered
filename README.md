#Commit Proposal

Matrikel Number: 15-649-239

Project idea short description:

My father works for the city of zurich and due to the nature of government institutions there are a lot of different departements and many employees, which all know eachother relatively well and have been working together for several years. Now, when it's getting 12 o'clock on a normal workday, they usually want to go eat something together, which can get rather chaotic. Some don't have much time, some have allergies or are vegan, some simply hate self-service restaurants like the Manor Mensa. It's not a surprise that my dad would like to optimize this process, and I think he's quite lucky to have me, an IT-student, as his son :). We discussed this idea a little bit and described some core features:

-people need to be able to fill in their name -they have to be able to fill in their idea on where to go -ideas entered once should be displayed in a dropdown menue for future user, so as to other users don't write another name for the same restaurant or have typos in their textfield -there should be a ranking, based on the most chosen restaurant -the app is day-based, which means there will be no calendar function, and the data stores would be deleted at midnight -participants only need the links and that's all (like doodle)

____additional features:

the app suggests groups if a lot of people enter different restaurants, those groups will eat at individual places
there should be a 'finito'-button which ends the survey and prompts a field in which the name of the person who is going to make the reservation should be entered (this person would be responsible)
the applikation can notify participants through windows notifications (not sure how to do that, but would be pretty cool!
the application should run on a real server and is usable for my father.

I'm especially interested in the use case showed on OpenDolphin's website "OpenDolphin for team applications", as people definitely need to be able to enter their suggestions simultaneously (and also monitor them in realtime).

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

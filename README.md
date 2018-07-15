# Codeforces-Status-Notifier
A tool to notify you when others submit or have status on Codeforces.

# How to use?

### Submit Notifier
When others submit and get a result, you will be notified and system will show you a popup dialog in the right-bottom corner. (Code at package `com.hhs.xgn.cftools.acn`)

Command Line: ```java -jar SubmitNoticer.jar Namelist CheckInterval```

*NameList* is a string of Codeforces usernames to check, spilt by spaces.

*CheckInterval* is the interval of checking by seconds, recommended 10.

You can start the program with no arguments and the program will ask you about the information.

### Moving Status
When others submit, you can see their status shown in the right-bottom corner too. (Code at package `com.hhs.xgn.cftools.ms`)

The window consists of four data: username problemId status passedTestCount

you can click on the labels to jump to the submission/problem/profile page quickly.

This actually covered the function of Submit Notifier. You can use this only.

Command Line: ```java -jar MovingStatus.jar Namelist```

*NameList* is a string of Codeforces usernames to check, spilt by spaces.

You can start the program with no arguments too and the program will ask you about the information.

**You can also see the samples.**

# How does it work?
Thanks for Mike and the wonderful Codefroces API, we can get the status by program.

# Current problems
Sometimes it won't work when you submit for the first time after the program runs. We currently don't know the reason.
We guess it's because of some net problems.

# WARNING
The problem data may doesn't work well with ACMSGURU or GYM! (Sometimes it will show contestId is -1)

# Installing
This is a full Eclipse project. You can just throw it into Eclipse. Then set all libraries in "lib" folder build path.

# Future
We may support more Online Judges and write a better version. :)

# Experimental functions
This tools also contain **User collect** and **Get Solution** tools.

**User collect** will collect your submission data and make some conclusions. 

**Get Soltuion** will download one's accepted solutions. 

**Experimental Functions are dangerous!Use them carefully**
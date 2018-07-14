# Codeforces-Status-Notifier
A tool to notify you when others submit or have status on Codeforces.

# How to use?

### Submit Notifier
When others submit, you will be notified.


Command Line: ```java -jar SubmitNoticer.jar Namelist CheckInterval```


*NameList* is a string of Codeforces usernames to check, spilt by spaces.

*CheckInterval* is the interval of checking by seconds, recommended 10.

### Moving Status
When others submit, you can see their status.

   This actually covered the function of Submit Notifier. You can use this only.

Command Line: java -jar ```SubmitNoticer.jar Namelist```


*NameList* is a string of Codeforces usernames to check, spilt by spaces.



**You can also see the samples.**



# How does it work?
Thank for the Codefroces API, we can get the status by program.



# Current problems
Sometimes it won't work when you submit for the first time after the program runs. We currently don't know the reason.
We guess it's because of some net problems.

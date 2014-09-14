

Geographical Team would handle : (GPS + Compass)

Responsibilities:
(1)Interface to a 5Hz or faster GPS
(2)Interface to a compass
(3)Allow a GPS coordinate to be "set"
(4)Based on the set coordinate, calculate, and provide CAN data regarding the current heading, and the desired heading to reach the destination 
(5)This unit needs to compute the "heading degree" to reach the destination 
(6)Provide Time and Date to set System time for all modules

Hardware:
(1)GPS      :Adafruit Ultimate GPS Breakout - 66 channel w/10 Hz updates (http://www.adafruit.com/product/746)
(2)Compass  :Adafruit Triple-axis Magnetometer (Compass) Board - HMC5883L (http://www.adafruit.com/products/1746)

*********************************************************************************************************************************
*********************************************************************************************************************************
Events:

(1)Basic CAN Infrastructure/Framework     09/18/2014
(2)



*********************************************************************************************************************************
*********************************************************************************************************************************
This is the each module source code is separated into a different branch with the prefix "team/<module>"
The source code development of this branch should only occur in user branches with prefix "<module>/user"
The most up-to-date module source code should live in the branch "<team>/<module>"

Any source code or file changes to the branch "<team>/<module>" should be done indirectly through merge requests from user branches on GitHub.

Any development of a new feature or fix for the specific module should have the following format:
"<module>/user/<your name>/<your new module feature or fix>"

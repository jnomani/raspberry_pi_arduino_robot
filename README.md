raspberry_pi_arduino_robot
==========================

This repo contains a Python script for serial communication through TCP/IP, Arduino code, and an Android Application for easy control.

DISCLAIMER: THis project is still a work in progress and should be tested at your own risk. I am not responsible for any damaged boards or equipment. 

Instructions:

Save the Python script in the home folder of the Pi, and make sure you have SSH enabled and ability to listen over Telnet port 7777.

Load the Arduino sketch onto any board (I used an Uno R3), change it according to your robot's setup. Make sure all pin numbers are correct, and the Adafruit Servo libraries are installed.

Load the android app onto your phone an voila! It might have to be tweaked a bit to get it to work with your setup.

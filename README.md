# Computer Networks Final Project
## By Almaamar Alkiyumi, Katy Hildebrant, and Quan Le
The purpose of this project is to implement a fully fledged client-server application (i.e., the bulletin board) using pure unicast sockets. You MUST use sockets regardless of the language you choose (no other built-in or third party libraries/modules are allowed for networking purposes). 
The project is divided into two parts. You should start with Part 1 first. Then you can extend your code to implement Part 2. Despite the fact that each part can be implemented separately (i.e., as two separate projects), you must not separate the implementation of both parts. In other words, you must submit one common code that can allow both parts to work seamlessly as intended during the same process execution (for example, users should
be able to switch between part 1 and part 2 without having to disconnect from the server).

# Part 1: A Public Message Board
Consider that all clients belong to one and only one public group. A client joins by connecting to a dedicated server (a standalone process) and is prompted to enter a non-existent user name in that group.

### Requirements
* The server listens on a specific non-system port endlessly. 
* The server keeps track of all users that join or leave the group.
* When a user joins or leaves the group, all other connected clients get notified by the server. 
* When a user (or client) joins the group, he/she can only see the last 2 messages that were posted on the board by other clients who joined earlier. 
* A list of users belonging to the group is displayed once a new user joins (in this part, the list represents all users/clients that have joined earlier). 
* When a user posts a new message, other users in the same group should see the posted message. 
* Messages are displayed in the following format: “Message ID, Sender, Post Date, Subject.” 
* A user can retrieve the content of a message by contacting the server and providing the message ID as a parameter.
* Your client program should also provide the option to leave the group. Once a user leaves the group, the server notifies all other users in the same group of this event.

# Part 2: Multiple Private Message Boards
Extend Part 1 to allow users to join multiple private groups. 

### Requirements
* Once a user is connected to the server, the server provides a list of 5 groups. 
* The user can then select the desired group by id or by name. 
* A user can join multiple groups at the same time. 
    * Remember that a user in one group cannot see users in other groups as well as the messages they have posted to their private board in other groups.

# Other Notes
* You are not required to implement any user authentication methods.
* Additional credit of 5% will be given if you have the client program written in a language different from the one used for the server program.
* Additional credit up to 5% may be given for a GUI implementation, based on its quality.

SocialNetwork
=============

READ-ME

EECS 293

Shaun Howard - smh150

Project 5: Linked With Social Network 

A user represents a user of the social network. The user consists of a unique
identifier to make sure that user is unique in the social network.

A Link connects two users and once established, it is never removed. Links can
be established and torn down on given dates, while the dates are not the same.

Activity of links can be determined by calling isActive(Date date) with a given date.

When a date is entered in isActive() that is before a tear down date or equal to or after
an establishment date, the link is returned as active, otherwise, it is inactive.

Dates of events in a link can be found by calling firstEvent() or nextEvent(Date date)
with a date that is in the range of activity of the link.

When a date is entered in nextEvent() that is before one event but after another, the date 
of the event that proceeds input date is returned.

A SocialNetwork contains the collection of all users in the social network and
 their links. Within a social network, you can establish links, tear down
links, get activity of links on given dates and determine if users are members.

An UninitializedObjectException is an exception that is thrown when any given part
of a social network is not initialized.

Overall, the Linked With social network accepts any user that is unique and can
link any two users given they are not already linked or they are not the same
user. On Linked With, one can check if a link is active on a given date and 
can establish or tear down links once established based on the fluctuality of
relationships between users.

**Newly added: Status variables implemented for various methods in the social network.
**Newly added: Can check the neighborhood for friends of a given user!
**Newly added: Find trends in the neighborhood of a given user with neighborhoodTrend()!

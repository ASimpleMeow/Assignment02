# Assignment02
A movie recommender program to recommend movies to users


It can add users, remove users, add movies, add ratings.
It also has top ten movies and user speicific top ten movies method
Top Ten Movies method will simply give the top ten movies based on the frequency in which
they appear in the user specific top ten list


The user specific top ten will just return a list of 10 movies that, that user rated hihest


The recommendations algorrithm I used is thus: 


The current user is compared to all other users (except himself) to determine his similarity to the 
other users based on the ratings that they gave to the same movie.


Then a map is created of Similarity Value --> User Object
It is sorted based on the similarity value.


Next, the highest similarity user goes first and submits his top ten list of movies.
If the current user hasn't rated a movies out of that list, that movie will be added to the 
recommendations. 


Finally the recommendations are returned.



The junit tests are written for every class except the Driver, Serializer interface, and Recommender interface.
All JUNIT tests pass as of 11/12/2016 - 17:40

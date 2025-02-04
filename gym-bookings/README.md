My thought process while solving this problem:

Initial analysis:
Create class API:
Input : 
- className
- startDate
- endDate
- startTime
- duration
- capacity

In this case the first approach was something like this:
I store 1 record in a table called classes with the above fields sent.
so each record will have class name, start date, end date, capacity of each possible class etc.
In this approach I don't have to store many records, just 1 - which is the input data.
If I have to find the overlaps, I can still verify with this data which is one approach.

But the main part is with the bookings.
If I receive a booking, my first basic validation to check is if the class on the requested date is available or not.
With the above table structure, it is not something that I can get easily with a simple operation.
Let's say, I received a booking request and for that I need to see how many bookings were done for that class on that date.
To check that I need to join both the tables somehow and see no of bookings already made for that date and subtract with the capacity
and check the no of slots left.
This is achievable but it's a little costly.

Because of the fact that the create class API is something that's not going to be called that frequently in practical scenarios
and the bookings API call would be done more frequently, I wanted to avoid that approach and followed a different one to make sure
that the bookings are very fast.

So, for every input of class, I store the class details per day.
So, if I have a class from 1st Jan to 5th Jan, I store 5 records in the table along with the capacity and slotsAvailable.
Now, if I get a booking request, it's easy for me to calculate it quickly.

CURLs that I have used for testing:

**-Create Class:**

_curl --location 'http://localhost:8080/api/v1/classes' \
--header 'Content-Type: application/json' \
--data '{
"name":"YOGA",
"startDate":"01-03-2025",
"endDate":"03-03-2025",
"startTime":"10:00",
"duration":120,
"capacity":30
}'_

Format used for date inputs - "dd-mm-yyyy"

Format used for time - "hh:mm" in 24 hour format

I created 2 APIs internally for testing purpose:
_curl --location 'http://localhost:8080/api/v1/classes' \
--data ''

curl --location 'http://localhost:8080/api/v1/classes/{className}' \
--data ''_

Since these two are for testing purpose, I did not add validations for them

**- Book Class:**

_curl --location 'http://localhost:8080/api/v1/bookings' \
--header 'Content-Type: application/json' \
--data '{
"memberName":"Anirudh",
"className":"YOGA",
"participationDate":"01-03-2025"
}'_

**- Get Bookings:**

_curl --location 'http://localhost:8080/api/v1/bookings?memberName=Anirudh' \
--data ''_

_curl --location 'http://localhost:8080/api/v1/bookings?memberName=Anirudh&startDate=01-03-2025&endDate=03-03-2025' \
--data ''_
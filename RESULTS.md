Results on MacBook Pro, PostgreSQL, without Apache
--------------------------------------------------

The test is the UIBK like test:
- login, open a course or the details page and
  return to "My courses" 5 five times, logout.
- 2000 users ramp up in 50 seconds
- 5 seconds between opening courses
- 50 seconds before logout

1. Standard configuration for OpenOLAT (chat, rating, comments)
   - Version: 10.0.2, 133 queries/s, 1% errors (timeout)


2. OpenOLAT without rating and comments but with chat
   - Version: 10.0.0, 180 queries/s, 0% errors (but there is some errors)
   
3. OpenOLAT without rating, comments and chat
   - Version: 10.0.0, 187 queries/s, 1% errors
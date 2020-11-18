# cafe-335

BeyondBrad's Loyalty program has been a big hit! The store is undergoing an expansion to their menu
and needs to update their Point of Sale system. They've added food items and a simple log to be 
able to data mine the day's sales to see how they're doing. But they want to do some more, and need
your help.

First, they've found that the employees sometimes mistype the menu items as people order and then
the whole system crashes. They'd really like to just have it print an error message and ask them to try again.

Second, they'd like to be able to add a query to the daily statistics to see how the food and drinks
are selling. They'd like a count of only the beverages to display after the other two statistics
print out.

Finally, they'd like to see how much money was spent on food that day. 


# What to do

- Add a new exception, InvalidMenuItemException, that is thrown by the Menu when the user enters
something that is not on the menu. Catch it in the view and prompt the user again.

- Add two new classes, FoodItem and Beverage, that extends MenuItem. For now, do not add any fields
to MenuItem, FoodItem, or Beverage. Make sure to update the existing items.

- Update the cash register to  print out the number of beverages sold by adding a new lambda 
function and calling runQuery. (Use ``instanceof``.)

- Update the cash register to print out the total amount of money spent on food by adding 
a new lambda function and calling runQuery.
 
- Produce a UML diagram that shows MenuItem, FoodItem, Beverage, and the five menu items. Use
draw.io and add the diagram to your repository before committing it.

The final commit pushed to GitHub prior to the due date graded

# DoorDash-Stats-Tracker

This is my Java Unit Project Project for Base Camp Coding Academy.
The idea behind the project is that I wanted to track the orders I get from DoorDash and view statistics based on those orders.
Such staticits might be what hours get the most orders, what restaurants get the most orders, and the total money made from orders by the restaraunt.

## Usage
Once the program is run, you are greeted with the main menu.
Here, you can record an order, view all orders, go to the search menu, delete an order, view the options, or quit.

If you choose to record an order, you will go through a form to fill out an order.
If you want to cancel, you can type `...` at any prompt.
Once the order is recorded, you will be brought back to the main menu.

If you choose to view all orders, you will see a paginated view of the orders.
Pressing enter will go to the next page, while typing q will go back to the main menu.
Once you reach the last page, you will be brought back to the main menu.

If you choose to delete an order, a list of all orders will be printed along with a number above it.
To delete an order, type the number that appears above the order.
If you want to cancel, you can type `...` instead.
After deleting an order, you will be brought back to the main menu.

If you choose to view the options, you will be brought to the options menu.
Here, you can select one of the options to change, and you will be prompted with how to change it.
Your changes will not be saved until you choose `Accept Changes`.
If you want to cancel changes, select cancel.

If you choose the search menu, you will be brought to the search menu.
This is where you can view statistics based on the recorded orders.
Each stat will display a leaderboard-style list, with the highest value being on top.
Note that the number of entries shown is limited by the `Search Order Rows` setting.

**CNT 4714 – Project 1 – Spring 2022**

**Objectives**:  To practice event-driven programming using Java-based GUIs. To refresh your basic 
Java skills. To simulate (albeit at a very high-level) an enterprise application. 
 
**Description**:  Develop a Java program that creates a standalone GUI application that simulates an e-
store (we’ll call our store Nile Dot Com... we’re not quite as big as Amazon.com!)1 which allows the 
user to add in stock items to a shopping cart and once all items are included, total all costs (including 
tax), produces an invoice, and append a transaction log file. 
 
 Your program development must include the following steps:   
1. Create a main GUI containing the following components: 
a. An area that allows the user to input data into the application along with the descriptive 
text that describes each input area. 
b. A total of six buttons as shown below with functionality as described below. 
c. As illustrated below, the various buttons on the interface are only accessible at certain 
points during a user’s interaction with the e-store.   
2. An input file named “inventory.txt”.  This is a comma separated file which contains the 
data that will be read by the application when the user makes a selection.  Each line in this file 
contains four entries; an item id (a string), a quoted string containing the description of the 
item, an in stock status (a string), and the unit price for one of the item (a double).  A sample 
file is provided for you on WebCourses.  Feel free to create your own input file for testing 
purposes or augment the provided input file. 
3. An output file (append only) named “transactions.txt” must be created that uniquely 
logs each user transaction with the e-store.  The unique transaction id will be generated as a 
permutation of the date/time string when the transaction occurred (see below). 
 
Restrictions: 
Your source file shall begin with comments containing the following information: 
/*  Name:  <your name goes here> 
     Course: CNT 4714 – Spring 2022 
     Assignment title: Project 1 – Event-driven Enterprise Simulation 
     Date: Sunday January 30, 2022 
*/ 
 
Input Specification:  The file “inventory.txt” as described above (see example below as 
well).  
 
Output Specification:  Output is to appear in the specified components of the GUI and various 
message  boxes  that  appear,  plus  the  contents  of  the 
“transactions.txt” log file that will be generated.  
 
 1 Source = https://www.wonderslist.com/top-10-largest-rivers/ 
CNT 4714 – Project 1 – Spring 2022 
  Page 2 
 
Deliverables: 
  Submit the following via WebCourses no later than 11:59pm Sunday January 30, 2022.  
  You can zip everything into a single folder if you would like. 
(1) A working copy of your source code (all .java files) and any necessary supporting libraries. 
(2) Include a file that contains the following nine screen shots.   
a. The initial GUI (like number 2 in Additional Information below). 
b. The GUI after the user has entered an item number (ID) and quantity and clicked the 
“Process Item #” button (like number 4 below). 
c. The pop-up message box when the user has confirmed the selected item (like number 
5 below). 
d. The pop-up message box when the user has entered an item number (ID) that was not 
found in the inventory file (like number 5 below). 
e. The GUI after the user has confirmed the selected item (like number 6 below). 
f. The GUI after the user has confirmed an item and has entered another item and 
processed it, but not yet confirmed it (like number 7 below). 
g. The message dialog box when the user clicks the “View Order” button with multiple 
items in the order (like number 9 below). 
h. The message dialog box when the user clicks the “Finish Order” button for an order 
containing multiple items (like number 10 below). 
i. The message dialog box when the user has entered an item number (ID) for an item 
that is out of stock (like number 12 below). 
(3) Include your “transactions.txt” file as it stands after several different transactions 
have been recorded.  You should have at least 5 different transactions in the file with at least 
three of these transactions having four or more items in the transaction. (similar to the one 
shown on the last page of this document). 

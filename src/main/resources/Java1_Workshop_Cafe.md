![Lexicon Logo](https://lexicongruppen.se/media/wi5hphtd/lexicon-logo.svg)

# Workshop – Café Order System

## The Scenario

You are building a console application for **Lexicon Café**.
The cashier uses the app to greet customers, take orders, apply discounts, and print receipts.
There is no database, no web interface — just a clean, working console program.

**Create `CafeApp.java`** inside `src/main/java/se/lexicon/`.
Commit regularly as you make progress.

---

## Requirements

Your application must:

- Display the café menu with item numbers, names, and prices
- Greet the customer by name
- Let the customer pick an item by number and choose a quantity
- Ask whether the customer is a loyalty member
- Calculate the bill according to these rules:
  - Base price: `unit price × quantity`
  - Discount rules (only one applies, member discount takes priority):
    - Loyalty member: **15% off** the base price
    - No membership but order exceeds **150 SEK**: **10% off**
  - VAT of **12%** is applied **after** the discount
- Print a formatted receipt showing every line: subtotal, discount (if any), VAT, and total
- Organise the logic into **methods** — `main` should only coordinate, not calculate

---

## Hints

Before writing any code, spend a few minutes thinking about the problem.

* What are the different responsibilities in this application?
* Which responsibility is the easiest to implement first?
* What information needs to flow from one part of the program to another?
* When a method needs information, should it calculate it itself or receive it as a parameter?
* If a piece of logic feels difficult to explain, can it be broken into smaller steps?

As you work:

* Build one small piece at a time.
* Try to keep each method focused on a single responsibility.
* If you notice the same logic appearing in multiple places, consider whether it belongs in its own method.
* Read your `main` method from top to bottom. Does it describe the application's workflow, or is it doing too much work itself?

> Think about the receipt from the bottom up: what values must exist before the final total can be printed?

---

## Sample Session

Below is a complete run of the finished application. Use it as your target — your output should match this for the same inputs.

**Test case: loyalty member, 2 lattes**

```text
Welcome! What is your name? Mehrdad
Hi Mehrdad! Here is our menu:

==============================
       Lexicon Cafe
==============================
1. Espresso         25.00 SEK
2. Cappuccino       35.00 SEK
3. Latte            40.00 SEK
4. Croissant        30.00 SEK
5. Sandwich         55.00 SEK
==============================

Enter item number (1-5): 3
How many? 2
Loyalty member? (yes/no): yes

==============================
      LEXICON CAFE
==============================
Customer  : Mehrdad
Item      : Latte x 2
Subtotal  : 80.00 SEK
Discount  : -12.00 SEK
VAT       : 8.16 SEK
------------------------------
TOTAL     : 76.16 SEK
==============================
   Thank you, Mehrdad!
   See you next time.
==============================
```

**Test case: no membership, large order (6 sandwiches)**

```text
Welcome! What is your name? Elnaz
Hi Elnaz! Here is our menu:
...
Enter item number (1-5): 5
How many? 6
Loyalty member? (yes/no): no

==============================
      LEXICON CAFE
==============================
Customer  : Elnaz
Item      : Sandwich x 6
Subtotal  : 330.00 SEK
Discount  : -33.00 SEK
VAT       : 35.64 SEK
------------------------------
TOTAL     : 332.64 SEK
==============================
   Thank you, Elnaz!
   See you next time.
==============================
```

**Test case: no discount (1 espresso, no membership)**

```text
Customer  : Simon
Item      : Espresso x 1
Subtotal  : 25.00 SEK
VAT       : 3.00 SEK
------------------------------
TOTAL     : 28.00 SEK
```

---

## Optional Challenges

Once the core app works, try extending it:  
- create a new branch for each challenge  
- commit your work regularly  
- merge your branch into the main branch when you're done  

### Challenge 1 – Serve Multiple Customers
Wrap the order flow in a loop. At the start of each round ask:
`"Next customer name (or 'done' to close): "`
When the cashier types `done`, stop the loop and print an end-of-day summary.

```text
==============================
      END OF DAY REPORT
==============================
Customers served : 3
Total revenue    : 437.28 SEK
==============================
```

### Challenge 2 – Input Validation & Error Handling
Guard every user input. If the cashier enters an invalid item number, a non-numeric value, or a quantity of zero or less, print a clear error message and re-prompt instead of crashing. Handle the loyalty membership field the same way — only `yes` or `no` are accepted.

> **Think about it:** Where is the best place to put validation logic — inside `main`, in a dedicated helper method, or somewhere else? What Java construct lets you catch unexpected input?

### Challenge 3 – Create an Order Class
Create a second file `Order.java` in the same package.
Move all order data (customer name, item, quantity, price, membership) into fields.
Move all calculations and receipt printing into instance methods.
In `main`, create a `new Order()` object, set its fields, and call `order.printReceipt()`.

> **Think about it:** In your current code, `printReceipt` probably needs several parameters. How many parameters does it need when the data lives in the object's own fields instead?

### Challenge 4 – Multiple Items per Order
Allow a single customer to order several different items before the receipt is printed.

Create a third file `LineItem.java` in the same package to represent one line on the receipt (item name, unit price, quantity, and a `lineTotal()` helper).

In `Order.java`, replace the single-item fields with an `ArrayList<LineItem>` and an `addItem()` method. Update all calculation methods to sum across every line item in the list.

In `CafeApp.java`, add an inner loop inside the customer turn. Show the menu once, ask for membership, then keep prompting for items until the cashier enters `0`. The discount and VAT rules are unchanged — they apply to the order subtotal as before.

```text
Next customer name (or 'done' to close): Mehrdad
Hi Mehrdad! Here is our menu:

==============================
       Lexicon Cafe
==============================
1. Espresso         25.00 SEK
2. Cappuccino       35.00 SEK
3. Latte            40.00 SEK
4. Croissant        30.00 SEK
5. Sandwich         55.00 SEK
==============================

Loyalty member? (yes/no): yes

Enter item number (1-5, or 0 to finish): 3
How many? 2
Latte added.

Enter item number (1-5, or 0 to finish): 4
How many? 1
Croissant added.

Enter item number (1-5, or 0 to finish): 0

==============================
      LEXICON CAFE
==============================
Customer  : Mehrdad
------------------------------
  Latte           x2    80.00 SEK
  Croissant       x1    30.00 SEK
------------------------------
Subtotal  : 110.00 SEK
Discount  : -16.50 SEK
VAT       : 11.22 SEK
------------------------------
TOTAL     : 104.72 SEK
==============================
   Thank you, Mehrdad!
   See you next time.
==============================
```

> **Think about it:** The `Order` class now holds a *list* of items. How does this change the way `calculateSubtotal()` works compared to the single-item version?

### Challenge 5 – Graphical User Interface
Replace the console interface with a desktop GUI using **Java Swing** or **JavaFX**.

> **Think about it:** How does the event-driven model of a GUI differ from the top-to-bottom flow of a console app? Which parts of your existing code can be reused without change?

## Submission

Push your project to GitHub and share the repository link with your instructor.
# Inventory Valuation Application (Hibernate with Panache and RESTEasy Reactive)

This is a minimal CRUD service exposing a couple of endpoints over REST so you can play with it from an application
of your own preference.

While the code is surprisingly simple, under the hood this is using:

- RESTEasy Reactive to expose the REST endpoints
- Hibernate Reactive with Panache to perform the CRUD operations on the database
- A MySQL database; see below to run one via Docker
- ArC, the CDI inspired dependency injection tool with zero overhead

## Understanding Stock and Inventory Valuation

Inventory valuation refers to the process of determining the value of inventory items held by a business before they are
sold. The value of inventory is an important factor in determining a company's financial position and profitability.
There are several different methods used to determine the value of inventory, each with its own advantages and
disadvantages.

* **Weighted Average Cost (WAC):** Calculates the average cost of all inventory items based on their purchase price and
  quantity. This method is often used in industries where inventory costs are relatively stable over time. The advantage
  of using the WAC method is that it is simple to calculate and provides a more accurate representation of the overall
  cost of inventory.
* **First-In, First-Out (FIFO):** It assumes that the first items purchased are the first items sold. This method is
  often used in industries where products have a short shelf life, such as the food industry. The advantage of using the
  FIFO method is that it ensures that older inventory is sold first, which can help prevent spoilage or obsolescence.
* **Last-In, First-Out (LIFO):** It assumes that the most recent items purchased are the first items sold. This method
  is often used in industries where inventory costs are rising, such as the petroleum industry. The advantage of using
  the LIFO method is that it can help reduce taxable income by matching higher-priced inventory with higher revenues,
  which can lead to lower taxes.
* **First Expiry, First-Out (FEFO):** It assumes that the items with the earliest expiration dates are the first to be
  sold or used. The advantage of using FEFO is that it can help to reduce waste and spoilage by ensuring that products
  that are close to their expiration dates are sold or used first.
* **Highest Cost, First-Out (HCFO):** The inventory items with the highest cost are assumed to be sold or used first.
  This method is often used in industries where the cost of inventory items can vary significantly, such as the
  automotive industry. The advantage of using the HCFO method is that it can help to maximize profits by matching
  higher-priced inventory with higher revenues.

## Understanding Selling Price Valuation

Determining the selling price of a stock is a critical aspect of any business that sells physical goods. The selling
price must be high enough to cover the cost of goods sold (COGS) while also generating a profit for the business.
However, it must also be competitive enough to attract customers and remain profitable in a competitive market.

In determining the selling price of a stock, calculate the COGS. Once the COGS is determined, the business can then add
a markup to cover additional expenses and generate a profit.

The markup can be a fixed percentage, such as 20%, or it can be calculated using a formula based on the desired profit
margin. The profit margin is the percentage of the selling price that represents the profit for the business after all
expenses are covered.

Or else, business can consider market trends and consumer demand, and arrive at a competitive and profitable selling
price for its products.

## Requirements

The project requirements can be found on the projects [README](../README.md) or you can refer to
the [Quarkus Official Website](https://quarkus.io).

### For more information visit:

* [Deskera](https://www.deskera.com/blog/inventory/)

-----
All documentation, source code and other files in this repository are Copyright 2023 Emmanuel Nika.
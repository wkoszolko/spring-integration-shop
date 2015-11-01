spring-integration-shop
====================
[![Build Status](https://travis-ci.org/wkoszolko/spring-integration-shop.svg?branch=master)](https://travis-ci.org/wkoszolko/spring-integration-shop)

This is very simple example of using Spring Integration DSL instead of XML based configuration. I used DSL to build uncomplicated integration flow for fictive online shop.

The pipeline
----
The pipeline:

1. Read incoming messages (orders) from order queue
2. Filter orders - check if order is valid
3. Split order to distinct items
4. Filter items - check if item is available
4. Route items to correct discount service
5. Count discounts
6. Create order confirmation
7. Put the confirmation in confirmation queue

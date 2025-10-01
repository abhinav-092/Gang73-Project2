--since order and order_summary both hold details of the order, we need to display both
SELECT * FROM orders WHERE orders.order_number = :num;
SELECT * FROM order_summary WHERE order_summary.order_number = :num;
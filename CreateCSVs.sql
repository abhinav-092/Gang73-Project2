\copy orders TO 'orders_out.csv' WITH (FORMAT csv, HEADER);

\copy menu_items TO 'menu_items_out.csv' WITH (FORMAT csv, HEADER);

\copy ingredients TO 'ingredients_out.csv' WITH (FORMAT csv, HEADER);

\copy inventory TO 'inventory_out.csv' WITH (FORMAT csv, HEADER);

\copy employees TO 'employees_out.csv' WITH (FORMAT csv, HEADER);

\copy order_summary TO 'order_summary_out.csv' WITH (FORMAT csv, HEADER);
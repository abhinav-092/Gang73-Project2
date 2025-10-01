SELECT
  menu_items.item_name,
  COUNT(ingredients.ingredient_ID) AS Ingredient_count
FROM ingredients, menu_items
WHERE menu_items.item_ID = ingredients.item_ID
GROUP BY ingredients.item_ID, menu_items.item_ID
ORDER BY Ingredient_count DESC;
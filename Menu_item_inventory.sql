SELECT
  menu_items.Item_name,
  COUNT(ingredients.Ingredient_ID) AS Ingredient_count
FROM ingredients, menu_items
WHERE menu_items.item_ID = ingredients.item_ID
GROUP BY ingredients.Item_ID, menu_items.Item_ID
ORDER BY Ingredient_count DESC;
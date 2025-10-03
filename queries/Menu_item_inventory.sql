SELECT
  menu_items.item_name,
  COUNT(ingredients.ingredient_ID) AS Ingredient_count --ingredients per item
FROM ingredients, menu_items
WHERE menu_items.item_ID = ingredients.item_ID --matching items
GROUP BY ingredients.item_ID, menu_items.item_ID --display per item
ORDER BY Ingredient_count DESC;
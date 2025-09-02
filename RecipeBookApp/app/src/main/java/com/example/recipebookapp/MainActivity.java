package com.example.recipebookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;  // Added missing import

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

/**
 * MainActivity
 * Displays recipes with filtering and a featured recipe section.
 * Remembers the last selected filter using SharedPreferences.
 */
public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String LAST_FILTER = "lastFilter";

    private LinearLayout recipeContainer;
    private RadioGroup mealTypeGroup;
    private TextInputEditText searchBox;
    private LinearLayout featuredRecipeContainer;  // Container for the featured recipe

    private String currentCategory = "all";

    // Array of recipe data: {Title, Ingredients, Category, YouTube ID, Web URL, Prep Time, Equipment, Instructions}
    private final String[][] recipes = {
            {"Classic Pancakes",
                    "• 200g all-purpose flour\n• 300ml milk\n• 1 large egg\n• 2 tbsp sugar\n• 2 tsp baking powder",
                    "Breakfast",
                    "tPLVNKgs8Lk",
                    "https://www.allrecipes.com/recipe/21014/good-old-fashioned-pancakes/",
                    "15 mins",
                    "Mixing bowl, Whisk, Non-stick pan",
                    "1. Mix dry ingredients\n2. Add wet ingredients\n3. Cook on medium heat\n4. Serve with maple syrup"},
            {"Berry Smoothie",
                    "• 1 banana\n• 150g mixed berries\n• 200ml almond milk\n• 1 tbsp honey",
                    "Breakfast",
                    "J83nEr7sg3c",
                    "https://www.allrecipes.com/recipe/135383/berry-delicious/",
                    "5 mins",
                    "Blender, Measuring cup",
                    "1. Add all ingredients\n2. Blend until smooth\n3. Serve chilled"},
            {"French Toast",
                    "• 4 slices bread\n• 2 eggs\n• 120ml milk\n• 1 tsp cinnamon",
                    "Breakfast",
                    "r1ZLSbQ0r0I",
                    "https://www.allrecipes.com/recipe/16895/fluffy-french-toast/",
                    "10 mins",
                    "Bowl, Pan, Spatula",
                    "1. Whisk eggs, milk, cinnamon\n2. Dip bread\n3. Fry until golden\n4. Serve warm"},
            {"Omelette",
                    "• 3 eggs\n• Salt & pepper\n• Cheese\n• Optional veggies",
                    "Breakfast",
                    "ixpYIgHlU60",
                    "https://www.allrecipes.com/recipe/262696/cheese-omelette/",
                    "5 mins",
                    "Bowl, Non-stick pan",
                    "1. Beat eggs\n2. Pour into hot pan\n3. Add fillings\n4. Fold and serve"},
            {"Homemade Pasta",
                    "• 300g 00 flour\n• 3 large eggs\n• 1 tbsp olive oil\n• Pinch of salt",
                    "Lunch",
                    "UfvrcHzv4TQ",
                    "https://www.allrecipes.com/recipe/11899/basic-pasta/",
                    "45 mins",
                    "Pasta roller, Large pot",
                    "1. Make flour well\n2. Knead dough\n3. Rest for 30 mins\n4. Roll and cut"},
            {"Caesar Salad",
                    "• Romaine lettuce\n• Croutons\n• Parmesan cheese\n• Caesar dressing",
                    "Lunch",
                    "IGlWE4AFQ5Q",
                    "https://www.allrecipes.com/recipe/229063/classic-restaurant-caesar-salad/",
                    "10 mins",
                    "Bowl, Knife",
                    "1. Chop lettuce\n2. Toss with dressing\n3. Top with croutons and cheese"},
            {"Chicken Salad",
                    "• 200g cooked chicken\n• Lettuce\n• Tomatoes\n• Salad dressing",
                    "Lunch",
                    "voHVFxMnMDw",
                    "https://www.allrecipes.com/recipe/8499/basic-chicken-salad/",
                    "15 mins",
                    "Knife, Bowl",
                    "1. Chop chicken\n2. Toss with veggies\n3. Add dressing\n4. Serve chilled"},
            {"Grilled Cheese",
                    "• 2 slices bread\n• Butter\n• Cheese slices",
                    "Lunch",
                    "aOJRJh1xU20",
                    "https://www.allrecipes.com/recipe/125434/grilled-cheese-of-the-gods/",
                    "5 mins",
                    "Pan, Spatula",
                    "1. Butter bread\n2. Place cheese between slices\n3. Grill until golden\n4. Slice and serve"},
            {"Beef Burger",
                    "• 500g ground beef\n• 4 burger buns\n• 1 onion\n• Lettuce\n• Cheese slices",
                    "Dinner",
                    "BIG1h2vG",
                    "https://www.allrecipes.com/recipe/25473/the-perfect-basic-burger/",
                    "25 mins",
                    "Grill pan, Spatula",
                    "1. Shape patties\n2. Season well\n3. Grill 4 mins/side\n4. Assemble burgers"},
            {"Grilled Salmon",
                    "• 2 salmon fillets\n• Lemon juice\n• Salt & pepper\n• Olive oil",
                    "Dinner",
                    "n7SnRm0RpdU",
                    "https://www.allrecipes.com/recipe/12720/grilled-salmon-i/",
                    "20 mins",
                    "Grill, Tongs",
                    "1. Marinate salmon\n2. Preheat grill\n3. Grill each side for 10 mins\n4. Serve with lemon"},
            {"Chicken Curry",
                    "• 500g chicken\n• Curry paste\n• Coconut milk\n• Onions\n• Peppers",
                    "Dinner",
                    "s2_DepctcIc",
                    "https://www.allrecipes.com/recipe/46822/indian-chicken-curry-ii/",
                    "30 mins",
                    "Pot, Spoon",
                    "1. Sauté onions, peppers\n2. Add chicken & curry paste\n3. Stir in coconut milk\n4. Simmer until cooked"},
            {"Veggie Stir Fry",
                    "• Mixed vegetables\n• Soy sauce\n• Garlic & ginger\n• Noodles or rice",
                    "Dinner",
                    "s8f752SdeAI",
                    "https://www.allrecipes.com/recipe/222660/stir-fried-vegetables/",
                    "20 mins",
                    "Wok, Spatula",
                    "1. Heat wok with oil\n2. Stir-fry garlic & ginger\n3. Add veggies & sauce\n4. Serve over rice or noodles"},
            {"Chocolate Cake",
                    "• 200g flour\n• 200g sugar\n• 100g cocoa powder\n• 1 tsp baking soda\n• 2 eggs",
                    "Dessert",
                    "GgOIdkV5PPQ",
                    "https://www.allrecipes.com/recipe/17370/chocolate-cake-ii/",
                    "50 mins",
                    "Oven, Mixing bowl, Cake pan",
                    "1. Preheat oven to 180°C\n2. Mix dry ingredients\n3. Add eggs & mix\n4. Bake for 35 mins"},
            {"Vanilla Ice Cream",
                    "• 500ml cream\n• 250ml milk\n• 100g sugar\n• 1 vanilla bean",
                    "Dessert",
                    "S7YGfsTmwsE",
                    "https://www.allrecipes.com/recipe/19812/vanilla-ice-cream-v/",
                    "30 mins",
                    "Ice cream maker, Mixing bowl",
                    "1. Heat milk & cream\n2. Stir in sugar & vanilla\n3. Chill mixture\n4. Process in ice cream maker"},
            {"Apple Pie",
                    "• Pie crust\n• 4 apples\n• Sugar & cinnamon\n• Butter",
                    "Dessert",
                    "KbyahTnzbKA",
                    "https://www.allrecipes.com/recipe/12682/apple-pie-by-grandma-ople/",
                    "60 mins",
                    "Oven, Pie dish",
                    "1. Slice apples\n2. Mix with sugar & cinnamon\n3. Fill crust & top with butter\n4. Bake for 40 mins"},
            {"Brownies",
                    "• 150g butter\n• 200g sugar\n• 75g cocoa powder\n• 2 eggs\n• 60g flour",
                    "Dessert",
                    "2Hw2pkJvdY0",
                    "https://www.allrecipes.com/recipe/10549/best-brownies/",
                    "35 mins",
                    "Oven, Mixing bowl, Baking tray",
                    "1. Melt butter\n2. Stir in sugar & cocoa\n3. Beat in eggs\n4. Add flour & bake 20 mins"},
            {"Avocado Toast",
                    "• 2 slices whole grain bread\n• 1 ripe avocado\n• Salt & pepper\n• Lemon juice",
                    "Breakfast",
                    "zq3tGsjcSgk",
                    "https://www.allrecipes.com/recipe/272611/avocado-toast/",
                    "5 mins",
                    "Toaster, Knife",
                    "1. Toast bread\n2. Mash avocado with salt, pepper, and lemon juice\n3. Spread on toast\n4. Serve immediately"},
            {"Quinoa Salad",
                    "• 1 cup quinoa\n• 2 cups water\n• 1 cucumber, diced\n• 1 tomato, diced\n• 1/4 cup olive oil\n• Lemon juice, salt & pepper",
                    "Lunch",
                    "L5DcwZpZZwU",
                    "https://www.allrecipes.com/recipe/229156/quinoa-salad/",
                    "20 mins",
                    "Pot, Knife, Bowl",
                    "1. Cook quinoa\n2. Chop vegetables\n3. Mix all ingredients\n4. Season with olive oil, lemon, salt, and pepper"},
            {"Spaghetti Bolognese",
                    "• 200g spaghetti\n• 250g ground beef\n• 1 onion, chopped\n• 2 cloves garlic, minced\n• 400g canned tomatoes\n• Basil, salt & pepper",
                    "Dinner",
                    "p1M9YXmDcCE",
                    "https://www.allrecipes.com/recipe/158140/spaghetti-sauce-with-ground-beef/",
                    "40 mins",
                    "Pot, Pan, Knife",
                    "1. Cook spaghetti\n2. Brown beef with onion and garlic\n3. Add tomatoes and simmer\n4. Mix with spaghetti and garnish with basil"},
            {"Grilled Chicken Sandwich",
                    "• 2 chicken breasts\n• 4 slices whole grain bread\n• Lettuce\n• Tomato slices\n• Mayonnaise, salt & pepper",
                    "Lunch",
                    "6SYG_M2o2mA",
                    "https://www.allrecipes.com/recipe/246106/grilled-chicken-sandwich/",
                    "30 mins",
                    "Grill, Knife",
                    "1. Season and grill chicken\n2. Toast bread\n3. Assemble sandwich with lettuce, tomato, and mayo\n4. Serve warm"},
            {"Smoothie Bowl",
                    "• 1 frozen banana\n• 1/2 cup frozen berries\n• 1/2 cup almond milk\n• Toppings: granola, coconut flakes",
                    "Breakfast",
                    "N8W28kV1RFo",
                    "https://www.allrecipes.com/recipe/270185/smoothie-bowl/",
                    "10 mins",
                    "Blender, Bowl, Spoon",
                    "1. Blend banana, berries, almond milk until smooth\n2. Pour into bowl\n3. Top with granola and coconut flakes\n4. Enjoy immediately"},
            {"Pumpkin Soup",
                    "• 500g pumpkin, cubed\n• 1 onion, chopped\n• 2 cloves garlic\n• 500ml vegetable broth\n• 1/2 cup coconut milk\n• Spices: nutmeg, salt, pepper",
                    "Dinner",
                    "Uo5XPjfqkEo",
                    "https://www.allrecipes.com/recipe/229960/pumpkin-soup/",
                    "35 mins",
                    "Pot, Blender, Knife",
                    "1. Sauté onion and garlic\n2. Add pumpkin and broth\n3. Simmer until soft\n4. Blend until smooth, stir in coconut milk, season with nutmeg, salt, and pepper"},
            {"Beet Salad",
                    "• 3 medium beets, roasted and cubed\n• 1/2 cup feta cheese\n• 1/4 cup walnuts\n• Mixed greens\n• Balsamic vinaigrette",
                    "Lunch",
                    "tZKfb3iNf7U",
                    "https://www.allrecipes.com/recipe/229156/beet-salad-with-feta/",
                    "25 mins",
                    "Oven, Knife, Bowl",
                    "1. Roast and cube beets\n2. Toss with greens, feta, and walnuts\n3. Drizzle with balsamic vinaigrette\n4. Serve chilled"},
            {"Banana Bread",
                    "• 3 ripe bananas\n• 2 cups flour\n• 1/2 cup sugar\n• 1/3 cup melted butter\n• 1 tsp baking soda\n• Pinch of salt",
                    "Dessert",
                    "GxBbN6F7-j4",
                    "https://www.allrecipes.com/recipe/20144/banana-banana-bread/",
                    "1 hr",
                    "Baking pan, Mixing bowl, Oven",
                    "1. Mash bananas\n2. Mix with melted butter, sugar, salt, and baking soda\n3. Stir in flour\n4. Bake at 350°F for 60 minutes"},
            {"Lemon Tart",
                    "• Tart crust\n• 1 cup lemon juice\n• 1/2 cup sugar\n• 3 eggs\n• 1/2 cup heavy cream",
                    "Dessert",
                    "z2l6Ns-KJSk",
                    "https://www.allrecipes.com/recipe/263732/lemon-tart/",
                    "1 hr",
                    "Tart pan, Oven, Mixing bowl",
                    "1. Prebake crust\n2. Mix lemon juice, sugar, eggs, and cream\n3. Pour into crust\n4. Bake until set, cool, and serve"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display a random featured recipe at each app launch
        displayFeaturedRecipe();

        // Restore last used filter from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastFilter = prefs.getString(LAST_FILTER, "");
        if (!lastFilter.isEmpty()) {
            currentCategory = lastFilter;
        }

        // Initialize UI elements
        recipeContainer = findViewById(R.id.recipeContainer);
        mealTypeGroup = findViewById(R.id.mealTypeGroup);
        Button clearFiltersBtn = findViewById(R.id.clearFiltersBtn);
        searchBox = findViewById(R.id.searchBox);

        // Apply initial filter
        applyFilter();

        // Listen for changes in the RadioGroup for filtering
        mealTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.breakfastBtn) {
                currentCategory = "Breakfast";
            } else if (checkedId == R.id.lunchBtn) {
                currentCategory = "Lunch";
            } else if (checkedId == R.id.dinnerBtn) {
                currentCategory = "Dinner";
            } else if (checkedId == R.id.dessertBtn) {
                currentCategory = "Dessert";
            } else if (checkedId == R.id.favoritesBtn) {  // New Favorites category
                currentCategory = "favorites";
            } else {
                currentCategory = "all";
            }
            // Save filter to SharedPreferences
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(LAST_FILTER, currentCategory)
                    .apply();
            applyFilter();
        });

        // Listen for text changes in the search box for real-time filtering
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    /**
     * Randomly selects one recipe from the array and displays it in the featured recipe section.
     */
    private void displayFeaturedRecipe() {
        // Get the container for the featured recipe
        featuredRecipeContainer = findViewById(R.id.featuredRecipeContainer);
        featuredRecipeContainer.removeAllViews();

        // Select a random recipe from the array
        Random random = new Random();
        int index = random.nextInt(recipes.length);
        String[] featuredRecipe = recipes[index];

        // Inflate the featured recipe layout (featured_recipe.xml)
        View featuredView = getLayoutInflater().inflate(R.layout.featured_recipe, featuredRecipeContainer, false);

        // Find views inside the featured recipe layout
        ImageView featuredImage = featuredView.findViewById(R.id.featuredImage);
        // Set the featured image to the new image resource (your placeholder)
        featuredImage.setImageResource(R.drawable.featured_recipe);
        TextView featuredTitle = featuredView.findViewById(R.id.featuredTitle);
        featuredTitle.setText(featuredRecipe[0]);

        // Set an onClickListener to open RecipeDetailActivity with the featured recipe's data
        featuredView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
            intent.putExtra("TITLE", featuredRecipe[0]);
            intent.putExtra("INGREDIENTS", featuredRecipe[1]);
            intent.putExtra("CATEGORY", featuredRecipe[2]);
            intent.putExtra("YOUTUBE_ID", featuredRecipe[3]);
            intent.putExtra("WEB_URL", featuredRecipe[4]);
            intent.putExtra("PREP_TIME", featuredRecipe[5]);
            intent.putExtra("EQUIPMENT", featuredRecipe[6]);
            intent.putExtra("INSTRUCTIONS", featuredRecipe[7]);
            startActivity(intent);
        });

        // Add the featured view to the container
        featuredRecipeContainer.addView(featuredView);
    }

    /**
     * Applies both the category filter and search filter to display matching recipes.
     */
    private void applyFilter() {
        String query = "";
        if (searchBox != null && searchBox.getText() != null) {
            query = searchBox.getText().toString().trim().toLowerCase();
        }
        recipeContainer.removeAllViews();

        // For Favorites filtering, you might access SharedPreferences ("Favorites") if needed

        for (String[] recipe : recipes) {
            String title = recipe[0];
            String ingredients = recipe[1];
            String category = recipe[2];

            boolean matchesCategory;
            // If "favorites" is selected, check SharedPreferences for this recipe's favorite status
            if (currentCategory.equals("favorites")) {
                SharedPreferences favPrefs = getSharedPreferences("Favorites", MODE_PRIVATE);
                matchesCategory = favPrefs.getBoolean(title, false);
            } else {
                matchesCategory = currentCategory.equals("all") || category.equalsIgnoreCase(currentCategory);
            }
            boolean matchesSearch = query.isEmpty() ||
                    title.toLowerCase().contains(query) ||
                    ingredients.toLowerCase().contains(query);

            if (matchesCategory && matchesSearch) {
                addRecipe(recipe);
            }
        }
    }

    /**
     * Inflates a recipe card (using recipe_card.xml) and adds it to the container.
     */
    private void addRecipe(String[] recipe) {
        View recipeCard = getLayoutInflater().inflate(R.layout.recipe_card, recipeContainer, false);
        TextView titleView = recipeCard.findViewById(R.id.cardTitle);
        TextView ingredientsView = recipeCard.findViewById(R.id.cardIngredients);

        titleView.setText(recipe[0]);
        ingredientsView.setText(recipe[1]);

        // When a recipe card is clicked, open the RecipeDetailActivity with the recipe data
        recipeCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
            intent.putExtra("TITLE", recipe[0]);
            intent.putExtra("INGREDIENTS", recipe[1]);
            intent.putExtra("CATEGORY", recipe[2]);
            intent.putExtra("YOUTUBE_ID", recipe[3]);
            intent.putExtra("WEB_URL", recipe[4]);
            intent.putExtra("PREP_TIME", recipe[5]);
            intent.putExtra("EQUIPMENT", recipe[6]);
            intent.putExtra("INSTRUCTIONS", recipe[7]);
            startActivity(intent);
        });
        recipeContainer.addView(recipeCard);
    }

    /**
     * Inflates the menu for the Toolbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handles selections from the Toolbar menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

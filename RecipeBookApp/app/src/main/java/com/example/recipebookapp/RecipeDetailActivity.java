package com.example.recipebookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

/**
 * RecipeDetailActivity
 * Displays detailed information about a selected recipe.
 * Allows users to mark a recipe as favorite, view ingredient substitutions (with allergen modes),
 * rate the recipe, and interact with additional features like video, website, call, and map.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private SharedPreferences favoritesPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Retrieve recipe data from the Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String ingredients = intent.getStringExtra("INGREDIENTS");
        String youtubeId = intent.getStringExtra("YOUTUBE_ID");
        String webUrl = intent.getStringExtra("WEB_URL");
        String prepTime = intent.getStringExtra("PREP_TIME");
        String equipment = intent.getStringExtra("EQUIPMENT");
        String instructions = intent.getStringExtra("INSTRUCTIONS");

        // Populate TextViews with recipe data
        setTextSafe(R.id.title, title, "No Title");
        setTextSafe(R.id.ingredients, ingredients, "No Ingredients Listed");
        setTextSafe(R.id.prepTime, "Prep Time: " + prepTime, "");
        setTextSafe(R.id.equipment, "Equipment Needed:\n" + equipment, "");
        setTextSafe(R.id.instructions, "Instructions:\n" + instructions, "");

        // Initialize YouTube player
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtubePlayer);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // Fallback to Rick Roll if video ID is null
                youTubePlayer.loadVideo(youtubeId != null ? youtubeId : "dQw4w9WgXcQ", 0);
            }
        });

        // Set up WebView for recipe website
        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(webUrl != null ? webUrl : "https://www.allrecipes.com");

        // Share Recipe button
        Button shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareText = "Check out this recipe: " + title + "\n" + webUrl;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
        });

        // Call Store button
        Button callBtn = findViewById(R.id.callBtn);
        callBtn.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+353876023980"));
            startActivity(dialIntent);
        });

        // Map button to open GroceryMapActivity
        Button mapBtn = findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, GroceryMapActivity.class));
        });

        // Initialize the Allergen Spinner
        Spinner allergenSpinner = findViewById(R.id.allergenSpinner);

        // Ingredient Substitution functionality with allergen modes
        Button substituteBtn = findViewById(R.id.btnSubstitute);
        substituteBtn.setOnClickListener(v -> {
            String selectedAllergen = allergenSpinner.getSelectedItem().toString();
            String substitutions;
            switch (selectedAllergen) {
                case "Dairy-Free":
                    substitutions = "Butter → Dairy-Free Margarine\nMilk → Almond Milk\nCheese → Dairy-Free Cheese";
                    break;
                case "Gluten-Free":
                    substitutions = "Flour → Gluten-Free Flour Mix\nBread → Gluten-Free Bread";
                    break;
                case "Nuts-Free":
                    substitutions = "Nut Milk → Oat Milk\nNut Oil → Sunflower Oil";
                    break;
                default:
                    substitutions = "Butter → Margarine\nMilk → Almond Milk\nSugar → Honey";
                    break;
            }
            new androidx.appcompat.app.AlertDialog.Builder(RecipeDetailActivity.this)
                    .setTitle("Ingredient Substitutions (" + selectedAllergen + ")")
                    .setMessage(substitutions)
                    .setPositiveButton("OK", null)
                    .show();
        });

        // Recipe Rating System functionality
        android.widget.RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) ->
                Toast.makeText(RecipeDetailActivity.this, "Rating saved: " + rating, Toast.LENGTH_SHORT).show());

        // ---------------------------
        // Favorite Toggle Functionality
        // ---------------------------
        // Initialize SharedPreferences for favorites (file name "Favorites")
        favoritesPrefs = getSharedPreferences("Favorites", MODE_PRIVATE);
        ToggleButton toggleFavorite = findViewById(R.id.toggleFavorite);
        // Set the toggle state based on whether the recipe is marked as a favorite
        boolean isFav = favoritesPrefs.getBoolean(title, false);
        toggleFavorite.setChecked(isFav);

        // Listener for the toggle button to mark or unmark favorites
        toggleFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = favoritesPrefs.edit();
            if (isChecked) {
                editor.putBoolean(title, true);
                Toast.makeText(RecipeDetailActivity.this, "Recipe marked as favorite", Toast.LENGTH_SHORT).show();
            } else {
                editor.remove(title);
                Toast.makeText(RecipeDetailActivity.this, "Recipe removed from favorites", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });
    }

    /**
     * Helper method to safely set text on a TextView.
     */
    private void setTextSafe(int viewId, String text, String defaultText) {
        TextView textView = findViewById(viewId);
        textView.setText(text != null ? text : defaultText);
    }
}

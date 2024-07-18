package com.example.task2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "QuotePrefs";
    private static final String KEY_QUOTE = "lastQuote";
    private static final String KEY_DATE = "lastDate";

    private TextView quoteTextView;
    private Button newQuoteButton;
    private Button shareButton;

    private String[] quotes = {
            "The only way to do great work is to love what you do. - Steve Jobs",
            "Life is what happens when you’re busy making other plans. - John Lennon",
            "The purpose of our lives is to be happy. - Dalai Lama",
            "Get busy living or get busy dying. - Stephen King",
            "You have within you right now, everything you need to deal with whatever the world can throw at you. - Brian Tracy"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quoteTextView);
        newQuoteButton = findViewById(R.id.newQuoteButton);
        shareButton = findViewById(R.id.shareButton);

        showDailyQuote();

        newQuoteButton.setOnClickListener(v -> showRandomQuote());
        shareButton.setOnClickListener(v -> shareQuote());
    }

    private void showDailyQuote() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastDate = prefs.getString(KEY_DATE, "");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if (!currentDate.equals(lastDate)) {
            int hash = currentDate.hashCode();
            int index = Math.abs(hash) % quotes.length;
            String newQuote = quotes[index];

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_QUOTE, newQuote);
            editor.putString(KEY_DATE, currentDate);
            editor.apply();

            quoteTextView.setText(newQuote);
        } else {
            String lastQuote = prefs.getString(KEY_QUOTE, quotes[0]);
            quoteTextView.setText(lastQuote);
        }
    }

    private void showRandomQuote() {
        int index = (int) (Math.random() * quotes.length);
        quoteTextView.setText(quotes[index]);
    }
    private void shareQuote() {
        String quote = quoteTextView.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, quote);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}
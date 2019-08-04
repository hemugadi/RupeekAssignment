package com.amit.mvvmnews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView name = findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("place_name"));

        TextView date = findViewById(R.id.date);
        date.setText(getIntent().getStringExtra("date"));

        String price = getIntent().getStringExtra("rate");

        TextView rate = findViewById(R.id.rate);
        rate.setText(getResources().getString(R.string.butn1_text,price));

        TextView description = findViewById(R.id.description);
        description.setText(getIntent().getStringExtra("description"));

        ImageView imageView = findViewById(R.id.image);
        Picasso.get().load(getIntent().getStringExtra("url")).into(imageView);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

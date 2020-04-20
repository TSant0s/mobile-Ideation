package com.example.ideation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IdeaPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_page);
        String ideaName = getIntent().getExtras().getString("name");
        String ideaDescription = getIntent().getExtras().getString("description");

        TextView idea_name = findViewById(R.id.textView_idea_name);
        TextView idea_description = findViewById(R.id.textView_idea_description);

        idea_name.setText(ideaName);
        idea_description.setText(ideaDescription);





    }
}

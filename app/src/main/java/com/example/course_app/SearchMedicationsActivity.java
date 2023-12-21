package com.example.course_app;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchMedicationsActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ListView searchResultsListView;

    private MedicationsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medications);

        searchEditText = findViewById(R.id.searchEditText);
        searchResultsListView = findViewById(R.id.searchResultsListView);

        databaseHelper = new MedicationsDatabaseHelper(this);
    }

    public void searchMedications(View view) {
        String query = searchEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(query)) {
            Cursor cursor = databaseHelper.searchMedications(query);

            if (cursor != null && cursor.getCount() > 0) {
                ArrayList<String> results = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String medicationName = cursor.getString(cursor.getColumnIndex(MedicationsDatabaseHelper.COLUMN_NAME));
                    String medicationDescription = cursor.getString(cursor.getColumnIndex(MedicationsDatabaseHelper.COLUMN_DESCRIPTION));
                    results.add("Name: " + medicationName + "\nDescription: " + medicationDescription);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
                searchResultsListView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Лекарства не найдены", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Введите запрос для поиска", Toast.LENGTH_SHORT).show();
        }
    }

    private void toMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

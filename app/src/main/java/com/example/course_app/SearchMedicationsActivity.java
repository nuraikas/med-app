package com.example.course_app;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchMedicationsActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ListView searchResultsListView;

    private MedicationsDatabaseHelper databaseHelper;
    private ImageButton imageButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medications);

        searchEditText = findViewById(R.id.searchEditText);
        searchResultsListView = findViewById(R.id.searchResultsListView);

        databaseHelper = new MedicationsDatabaseHelper(this);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainActivity();
            }
        });
    }

    public void searchMedications(View view) {
        String query = searchEditText.getText().toString().trim();
        // Проверяем, что введенный запрос не пуст
        if (!TextUtils.isEmpty(query)) {
            // Выполняем поиск в базе данных по введенному запросу
            Cursor cursor = databaseHelper.searchMedications(query);
            // Проверяем, найдены ли какие-либо лекарства
            if (cursor != null && cursor.getCount() > 0) {
                // Создаем список для хранения результатов поиска
                ArrayList<String> results = new ArrayList<>();
                // Перебираем результаты из Cursor
                while (cursor.moveToNext()) {
                    // Извлекаем название и описание лекарства из Cursor
                    String medicationName = cursor.getString(cursor.getColumnIndex(MedicationsDatabaseHelper.COLUMN_NAME));
                    String medicationDescription = cursor.getString(cursor.getColumnIndex(MedicationsDatabaseHelper.COLUMN_DESCRIPTION));
                    // Формируем строку с информацией о лекарстве и добавляем в список результатов
                    results.add("Name: " + medicationName + "\nDescription: " + medicationDescription);
                }
                // Создаем адаптер для отображения результатов в ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
                // Устанавливаем адаптер для ListView, чтобы отобразить результаты поиска
                searchResultsListView.setAdapter(adapter);
            } else {
                // Если ничего не найдено, выводим сообщение пользователю
                Toast.makeText(this, "Лекарства не найдены", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Если запрос пуст, напоминаем пользователю ввести текст для поиска
            Toast.makeText(this, "Введите запрос для поиска", Toast.LENGTH_SHORT).show();
        }
    }
    // функция перехода на главную страницу
    private void toMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

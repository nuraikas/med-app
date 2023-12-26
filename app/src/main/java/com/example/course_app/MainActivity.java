package com.example.course_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView plansListView;
    private ArrayList<String> listOfPlans;
    private Button addDb;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.imageButton);
        plansListView = findViewById(R.id.plansListView);
        addDb = findViewById(R.id.addDb);
        // Получаем список планов из SharedPreferences
        listOfPlans = getPlansFromSharedPreferences();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfPlans);
        plansListView.setAdapter(adapter);
        // Установим слушатель щелчка на элемент списка
        plansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewMedications(position);
            }
        });
        // Удаление плана при долгом нажатии
        plansListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String planToRemove = listOfPlans.get(position);
                removePlanFromSharedPreferences(planToRemove);
                return true;
            }
        });
        // Обработка события нажания на кнопку для добавления лекарства
        addDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedicationToDatabase();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMed();
            }
        });
    }
    // Функция перехода на страницу создания нового плана
    public void createPlan(View v) {
        Intent intent = new Intent(this, createPlan.class);
        startActivity(intent);
    }
    // Фунция отображения списка лекарств
    public void viewMedications(int position) {

        if (position != ListView.INVALID_POSITION) {
            String selectedPlan = listOfPlans.get(position);

            // Переходим на активити для отображения лекарств в выбранном плане
            Intent intent = new Intent(this, planDetails.class);
            intent.putExtra("selectedPlan", selectedPlan);
            startActivity(intent);
        }
    }

    private ArrayList<String> getPlansFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(createPlan.PREFS_NAME, MODE_PRIVATE);

        // Получаем список планов из SharedPreferences
        Set<String> planSet = sharedPreferences.getStringSet("plans", new HashSet<String>());
        return new ArrayList<>(planSet);
    }

    private void removePlanFromSharedPreferences(String planToRemove) {
        SharedPreferences sharedPreferences = getSharedPreferences(createPlan.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Получаем текущий список планов
        Set<String> existingPlans = sharedPreferences.getStringSet("plans", new HashSet<String>());

        // Удаляем выбранный план из списка
        existingPlans.remove(planToRemove);

        // Удаляем данные для выбранного плана
        editor.remove(planToRemove);

        // Сохраняем обновленный список планов
        editor.putStringSet("plans", existingPlans);

        // Применяем изменения
        editor.apply();

        // Обновляем отображение списка планов
        listOfPlans = getPlansFromSharedPreferences();
        updateListView();
    }
    //Функция обновления списка планов
    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfPlans);
        plansListView.setAdapter(adapter);
    }
    // Функция добавления лекаства в базу данных
    private void addMedicationToDatabase() {
        Intent intent = new Intent(this, AddMedicationActivity.class);
        startActivity(intent);
    }
    // Функция перехода на страницу поиска лекарств
    private void searchMed(){
        Intent intent = new Intent(this, SearchMedicationsActivity.class);
        startActivity(intent);
    }
}
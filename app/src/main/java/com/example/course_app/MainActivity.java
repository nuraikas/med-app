package com.example.course_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView plansListView;
    private ArrayList<String> listOfPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plansListView = findViewById(R.id.plansListView);

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

        plansListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String planToRemove = listOfPlans.get(position);
                removePlanFromSharedPreferences(planToRemove);
                return true;
            }
        });
    }

    public void createPlan(View v) {
        Intent intent = new Intent(this, createPlan.class);
        startActivity(intent);
    }

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

    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfPlans);
        plansListView.setAdapter(adapter);
    }

    private void addMedicationToDatabase() {
        Intent intent = new Intent(this, AddMedicationActivity.class);
        startActivity(intent);
    }
}
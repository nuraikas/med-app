package com.example.course_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class planDetails extends AppCompatActivity {

    private ListView medicationsListView;
    private ArrayList<String> medicationsList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_details);

        medicationsListView = findViewById(R.id.medicationsListView);

        // Получаем название выбранного плана из Intent
        String selectedPlan = getIntent().getStringExtra("selectedPlan");

        // Получаем список лекарств для выбранного плана из SharedPreferences
        medicationsList = getMedicationsFromSharedPreferences(selectedPlan);

        setTitle("Лекарства для " + selectedPlan);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationsList);
        medicationsListView.setAdapter(adapter);

        medicationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String medicationToRemove = medicationsList.get(position);
                removeMedicationFromSharedPreferences(selectedPlan, medicationToRemove);
                return true; // обработали событие
            }
        });

    }
    private ArrayList<String> getMedicationsFromSharedPreferences(String selectedPlan) {
        SharedPreferences sharedPreferences = getSharedPreferences(createPlan.PREFS_NAME, MODE_PRIVATE);

        // Получаем список лекарств для выбранного плана из SharedPreferences
        Set<String> medicationsSet = sharedPreferences.getStringSet(selectedPlan, new HashSet<String>());
        return new ArrayList<>(medicationsSet);
    }

    private void removeMedicationFromSharedPreferences(String selectedPlan, String medicationToRemove) {
        SharedPreferences sharedPreferences = getSharedPreferences(createPlan.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Получаем текущий список лекарств для выбранного плана
        Set<String> medicationsSet = sharedPreferences.getStringSet(selectedPlan, new HashSet<String>());

        // Удаляем выбранное лекарство из списка
        medicationsSet.remove(medicationToRemove);

        // Сохраняем обновленный список лекарств для выбранного плана
        editor.putStringSet(selectedPlan, medicationsSet);

        // Применяем изменения
        editor.apply();

        // Обновляем отображение списка лекарств
        medicationsList = getMedicationsFromSharedPreferences(selectedPlan);
        updateListView();
    }

    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationsList);
        medicationsListView.setAdapter(adapter);
    }
    public void editPlan(View view) {
        // Получаем название плана
        String planName = getIntent().getStringExtra("selectedPlan");

        // Запускаем CreatePlanActivity для редактирования плана
        Intent intent = new Intent(this, createPlan.class);
        intent.putExtra("editPlan", true);
        intent.putExtra("planToEdit", planName);
        startActivity(intent);
    }
}

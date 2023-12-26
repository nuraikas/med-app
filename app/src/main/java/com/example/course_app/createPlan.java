package com.example.course_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class createPlan extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";

    private EditText planNameEditText;
    private LinearLayout medicationsLayout;
    private ArrayList<EditText> medicationsEditTextList;
    private Button saveButton;
    private Button addDrug;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cerate_plan);

        planNameEditText = findViewById(R.id.planNameEditText);
        medicationsLayout = findViewById(R.id.medicationsLayout);
        saveButton = findViewById(R.id.saveButton);

        medicationsEditTextList = new ArrayList<>();

        // Проверяем, редактируем ли существующий план
        boolean editPlan = getIntent().getBooleanExtra("editPlan", false);
        if (editPlan) {
            String planToEdit = getIntent().getStringExtra("planToEdit");
            loadPlanData(planToEdit);
        }
        // При нажатии на кнопку сохраняем и переходим на главную страницу
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePlan();
                goBack(view);
            }
        });
    }
    // функция добавления новых инпутов для лекарств
    public void addEditText(View view) {
        EditText newEditText = new EditText(this);
        newEditText.setHint("Введите название лекарства");
        medicationsLayout.addView(newEditText);
        medicationsEditTextList.add(newEditText);
    }
    public void savePlan() {
        String planName = planNameEditText.getText().toString();
        ArrayList<String> medicationsList = new ArrayList<>();

        for (EditText editText : medicationsEditTextList) {
            medicationsList.add(editText.getText().toString());
        }

        // Сохраняем в SharedPreferences
        saveToSharedPreferences(planName, medicationsList);

        finish(); // Закрываем активити после сохранения
    }

    private void saveToSharedPreferences(String planName, ArrayList<String> medicationsList) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Получаем текущий список планов
        Set<String> existingPlans = sharedPreferences.getStringSet("plans", new HashSet<String>());

        // Добавляем новый план в список
        existingPlans.add(planName);

        // Сохраняем обновленный список планов
        editor.putStringSet("plans", existingPlans);

        // Сохраняем список лекарств для текущего плана
        editor.putStringSet(planName, new HashSet<>(medicationsList));

        // Применяем изменения
        editor.apply();
    }
    // функция переходп на главную страницу
    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void loadPlanData(String planName) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Загрузка данных из SharedPreferences
        Set<String> medicationsSet = sharedPreferences.getStringSet(planName, new HashSet<String>());

        planNameEditText.setText(planName);

        // Очищаем список перед загрузкой
        medicationsEditTextList.clear();
        medicationsLayout.removeAllViews();

        for (String medication : medicationsSet) {
            addEditTextWithText(medication);
        }
    }
    // функция отображения уже сохраненных лекарств
    private void addEditTextWithText(String text) {
        EditText newEditText = new EditText(this);
        newEditText.setHint("Введите название лекарства");
        newEditText.setText(text);
        medicationsLayout.addView(newEditText);
        medicationsEditTextList.add(newEditText);
    }

}

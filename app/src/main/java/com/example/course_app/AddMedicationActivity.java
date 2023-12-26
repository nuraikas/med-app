package com.example.course_app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMedicationActivity extends AppCompatActivity {

    private EditText medicationNameEditText;
    private EditText medicationDescriptionEditText;

    private MedicationsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medication);

        medicationNameEditText = findViewById(R.id.medicationNameEditText);
        medicationDescriptionEditText = findViewById(R.id.medicationDescriptionEditText);

        databaseHelper = new MedicationsDatabaseHelper(this);
    }

    public void addMedication(View view) {
        // Получение текста из полей ввода для названия и описания лекарства
        String medicationName = medicationNameEditText.getText().toString().trim();
        String medicationDescription = medicationDescriptionEditText.getText().toString().trim();

        // Проверка, что название лекарства не пустое
        if (!TextUtils.isEmpty(medicationName)) {
            // Вызов метода для добавления лекарства в базу данных
            databaseHelper.addMedication(medicationName, medicationDescription);

            // Отображение сообщения об успешном добавлении
            Toast.makeText(this, "Лекарство успешно добавлено", Toast.LENGTH_SHORT).show();

            // Очистка полей ввода
            clearFields();
        } else {
            // Если название лекарства пустое, напоминаем пользователю ввести название
            Toast.makeText(this, "Введите название лекарства", Toast.LENGTH_SHORT).show();
        }
    }
    // Метод для очистки полей ввода
    private void clearFields() {
        medicationNameEditText.getText().clear();
        medicationDescriptionEditText.getText().clear();
    }

}

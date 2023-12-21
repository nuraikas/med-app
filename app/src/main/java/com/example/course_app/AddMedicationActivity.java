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
        String medicationName = medicationNameEditText.getText().toString().trim();
        String medicationDescription = medicationDescriptionEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(medicationName)) {
            databaseHelper.addMedication(medicationName, medicationDescription);
            Toast.makeText(this, "Лекарство успешно добавлено", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Введите название лекарства", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        medicationNameEditText.getText().clear();
        medicationDescriptionEditText.getText().clear();
    }
}

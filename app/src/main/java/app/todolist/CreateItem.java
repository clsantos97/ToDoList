package app.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateItem extends AppCompatActivity implements Button.OnClickListener {

    private Button btnCreate;
    private EditText etName;
    private EditText etDate;
    private EditText etTime;
    private EditText etTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        // TODO Get from strings
        setTitle("Crear Item");

        // Init gui elements
        etName = (EditText) findViewById(R.id.etName);
        etDate = (EditText) findViewById(R.id.etDate);
        etTime = (EditText) findViewById(R.id.etTime);
        etTask = (EditText) findViewById(R.id.etTask);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ((Button) view == btnCreate) {
            // TODO EditText Validations
            Intent i = new Intent();

            i.putExtra("NAME", etName.getText().toString());
            i.putExtra("TASK", etTask.getText().toString());
            i.putExtra("DATE", etDate.getText().toString());
            i.putExtra("TIME", etTime.getText().toString());

            setResult(RESULT_OK, i);
            finish();

        }
    }
}

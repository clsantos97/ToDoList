package app.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private static final Logger logger = Logger.getLogger( MainActivity.class.getName() );
    private Button btnAdd;
    private ListView lvToDo;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init gui elements
        btnAdd = (Button) findViewById(R.id.btnAdd);
        lvToDo = (ListView) findViewById(R.id.lvToDo);

        // Add listeners
        btnAdd.setOnClickListener(this);

        // Init DB
        db = openOrCreateDatabase("ToDoListDb", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS item(id NUMBER,name VARCHAR,task VARCHAR, date VARCHAR, time VARCHAR);");

        if (!isDbEmpty()) {
            insertMockData();
        }

        listar();

    }

    @Override
    public void onClick(View view) {
        if ((Button) view == btnAdd) { // EN ESTA VENTANA
            Intent intent = new Intent(this, CreateItem.class);
            //intent.putExtra("mensaje", mensaje);
            startActivity(intent);
        }
    }

    /**
     * Muestra listado de discos
     */
    public void listar() {
        ArrayAdapter<String> adaptador;
        List<String> lista = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT * FROM item", null);

        if (c.getCount() == 0) {
            lista.add("No hay registros");
        } else {
            while (c.moveToNext())
                lista.add(c.getString(1) + "-" + c.getString(2)+"  "+ c.getString(3)+"  "+ c.getString(4));
        }

        adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.standard_listview, lista);
        lvToDo.setAdapter(adaptador);
        c.close();
    }

    public boolean isDbEmpty() {
        boolean res;
        Cursor c = db.rawQuery("SELECT * FROM item", null);

        if (c.getCount() == 0) {
            res = true;
        } else {
            res = false;
        }
        c.close();
        return res;
    }

    public void insertMockData() {

        try {
            for(int i=0;i<5;i++){
                db.execSQL("INSERT INTO item VALUES (" + i+1 + ",'" + "Task_"+i + "','" + "08/11/2017"+"','" + "17:00"+"')");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error al añadir datos de prueba");
        }
    }
}

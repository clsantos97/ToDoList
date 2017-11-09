package app.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());
    private Button btnAdd;
    private ListView lvToDo;
    private SQLiteDatabase db;
    public static final int CREATE_ITEM = 1;

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

        //db.execSQL("DROP TABLE item");
        db.execSQL("CREATE TABLE IF NOT EXISTS item(" +
                "item_id INTEGER PRIMARY KEY,name VARCHAR,task VARCHAR, date VARCHAR, time VARCHAR);");
        db.execSQL("DELETE FROM item");

        if (!isDbItemsEmpty()) {
            insertMockData();
        }
        listar();
    }

    @Override
    public void onClick(View view) {
        if ((Button) view == btnAdd) { // EN ESTA VENTANA
            Intent intent = new Intent(this, CreateItem.class);
            startActivityForResult(intent, CREATE_ITEM);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CREATE_ITEM) {
            if (resultCode == RESULT_OK) {
                // se seleccionó correctamente la provincia
                //t.setText("Se ha seleccionado:\n"+data.getStringExtra("PROVINCIA"));
                try {
                    db.execSQL("INSERT INTO item (name, task, date, time) VALUES ('" + data.getStringExtra("NAME") + "','" +
                            data.getStringExtra("TASK") + "','" +
                            data.getStringExtra("DATE") + "','" +
                            data.getStringExtra("TIME") + "')");

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("New task insert error.");
                }

                listar();
            } else {
                logger.info("New res task insert error.");
            }
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
                lista.add(c.getString(0) + " " + c.getString(1) + " - " + c.getString(2) + "   " + c.getString(3) + "  " + c.getString(4));
        }

        adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.standard_listview, lista);
        lvToDo.setAdapter(adaptador);
        c.close();
    }

    public boolean isDbItemsEmpty() {
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
            for (int i = 0; i < 5; i++) {
                try {
                    //db.execSQL("INSERT INTO item VALUES (" + i + 1 + ",'" + "Task" + "','" + "Task Description" + "','" + "08/11/2017" + "','" + "17:00" + "')");
                    db.execSQL("INSERT INTO item (name, task, date, time) VALUES ('Task','TaskDescription','08/11/2017','18:00')");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Error al insertar mockdata.");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error al añadir datos de prueba");
        }
    }


}

package com.gonzalezp.apps.androidfunwithflags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class psgs_crudClients extends AppCompatActivity {

    private EditText editTextCodigo;
    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextCorreo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.psgs_activity_main);

        editTextCodigo = findViewById(R.id.editTextCodigo);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextCorreo = findViewById(R.id.editTextCorreo);
    }

    public void onClicInsertar(View view){
        //this.InsertarSinClase();
        this.InsertarConClase();
    }
    private void InsertarConClase() {

        psgs_clientDal clienteDAL = new psgs_clientDal(this);

        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String correo = editTextCorreo.getText().toString();

        if(!nombre.equals("") && !apellido.equals("") && !correo.equals("")){
            psgs_Client cliente = new psgs_Client();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setCorreo(correo);

            clienteDAL.insertDAL(cliente);

            editTextNombre.setText("");
            editTextApellido.setText("");
            editTextCorreo.setText("");

            Toast.makeText(this, "Cliente insertado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Todos los campos son Obligatorios", Toast.LENGTH_SHORT).show();
        }
    }
    public void InsertarSinClase(){
        psgs_clientsHelper clientesHelper = new psgs_clientsHelper(this,"ClientesDB",null,1);
        // abrir la DB
        //SQLiteDatabase sql = clientesHelper.getReadableDatabase(); // lectura
        SQLiteDatabase sql = clientesHelper.getWritableDatabase(); // escritura

        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String correo = editTextCorreo.getText().toString();

        if(!nombre.equals("") && !apellido.equals("") && !correo.equals("")){
            //
            //sql.execSQL("INSERT INTO Clientes (Nombre,Apellido,Correp) VALUES("+nombre+","+apellido+","+correo+")");
            // crear la coleccion de datos
            ContentValues listaValores = new ContentValues();
            listaValores.put("Nombre",nombre);
            listaValores.put("Apellido",apellido);
            listaValores.put("Correo",correo);
            //enviar a la DB
            sql.insert("Clientes",null,listaValores);
            //cerra la DB
            sql.close();

            editTextNombre.setText("");
            editTextApellido.setText("");
            editTextCorreo.setText("");

            Toast.makeText(this, "Cliente insertado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Todos los campos son Obligatorios", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClicEditar(View view){
        //this.editarSinClase();
        this.editarConClase();

    }
    public void editarConClase(){

        psgs_clientDal clienteDAL = new psgs_clientDal(this);
        String codigo = editTextCodigo.getText().toString();
        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String correo = editTextCorreo.getText().toString();

        psgs_Client cliente = new psgs_Client();
        cliente.setCodigo(Integer.valueOf(codigo));
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setCorreo(correo);

        int cantidad=clienteDAL.updateDAL(cliente);

        if(cantidad>0){
            Toast.makeText(this, "Registro Modificado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "El registro no se pudo Modificar", Toast.LENGTH_SHORT).show();
        }
    }
    public void editarSinClase(){
        psgs_clientsHelper clientesHelper = new psgs_clientsHelper(this,"ClientesDB",null,1);
        // abrir la DB
        //SQLiteDatabase sql = clientesHelper.getReadableDatabase(); // lectura
        SQLiteDatabase sql = clientesHelper.getWritableDatabase(); // escritura

        String codigo = editTextCodigo.getText().toString();
        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String correo = editTextCorreo.getText().toString();

        ContentValues listaValores = new ContentValues();
        listaValores.put("Codigo",codigo);
        listaValores.put("Nombre",nombre);
        listaValores.put("Apellido",apellido);
        listaValores.put("Correo",correo);

        int cantidad=sql.update("Clientes",listaValores,"Codigo="+codigo,null);

        sql.close();

        if(cantidad>0){
            Toast.makeText(this, "Registro Modificado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "El registro no se pudo Modificar", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClicEliminar(View view){
        this.eliminarConClase();
        //this.eliminarSinClase();
    }
    public void eliminarConClase(){

        psgs_clientDal clienteDAL = new psgs_clientDal(this);
        String codigo = editTextCodigo.getText().toString();

        int cantidad = clienteDAL.deleteDAL(Integer.valueOf(codigo));

        if(cantidad>0){
            Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
        }

        editTextNombre.setText("");
        editTextApellido.setText("");
        editTextCorreo.setText("");
    }
    public void eliminarSinClase(){
        psgs_clientsHelper clientesHelper = new psgs_clientsHelper(this,"ClientesDB",null,1);
        // abrir la DB
        //SQLiteDatabase sql = clientesHelper.getReadableDatabase(); // lectura
        SQLiteDatabase sql = clientesHelper.getWritableDatabase(); // escritura

        String codigo = editTextCodigo.getText().toString();

        int cantidad = sql.delete("Clientes","Codigo="+codigo,null);

        if(cantidad>0){
            Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
        }

        editTextNombre.setText("");
        editTextApellido.setText("");
        editTextCorreo.setText("");
    }

    public void onClicBuscar(View view){
        this.buscarConClase();
        //this.buscarSinClase();
    }
    public void buscarConClase(){

        psgs_clientDal clienteDAL = new psgs_clientDal(this);
        String codigo = editTextCodigo.getText().toString();

        psgs_Client cliente = new psgs_Client();
        cliente=clienteDAL.selectByCodigoDAL(Integer.valueOf(codigo));

        if(cliente!=null)
        {
            editTextNombre.setText(cliente.getNombre());
            editTextApellido.setText(cliente.getApellido());
            editTextCorreo.setText(cliente.getCorreo());
        }
        else
        {
            Toast.makeText(this, "No se encontraron registros en la tabla", Toast.LENGTH_SHORT).show();
        }

    }
    public void buscarSinClase(){
        psgs_clientsHelper clientesHelper = new psgs_clientsHelper(this,"ClientesDB",null,1);
        // abrir la DB
        //SQLiteDatabase sql = clientesHelper.getReadableDatabase(); // lectura
        SQLiteDatabase sql = clientesHelper.getWritableDatabase(); // escritura

        String codigo = editTextCodigo.getText().toString();

        //String consulta = "SELECT * FROM Clientes";
        String consulta = "SELECT Codigo, Nombre, Apellido, Correo FROM Clientes WHERE Codigo="+codigo;

        Cursor cursor = sql.rawQuery(consulta, null);
        // ciclo where
        if(cursor.moveToFirst())
        {
            editTextNombre.setText(cursor.getString(1));
            editTextApellido.setText(cursor.getString(2));
            editTextCorreo.setText(cursor.getString(3));
        }
        else
        {
            Toast.makeText(this, "No se encontraron registros en la tabla", Toast.LENGTH_SHORT).show();
        }
        sql.close();
    }
}

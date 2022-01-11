package com.gonzalezp.apps.androidfunwithflags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class psgs_clientDal {
    private psgs_clientsHelper clientesHelper; // crea la BD
    private SQLiteDatabase sql;
    private Context context;

    public psgs_clientDal(psgs_crudClients context){
        this.context=context;
    }

    public void openDAL(){
        clientesHelper=new psgs_clientsHelper(context,"ClientesDB",null,1);
        sql=clientesHelper.getWritableDatabase();
    }

    public void closeDAL(){
        sql.close();
    }

    public long insertDAL(psgs_Client cliente){
        long count =0;
        try {
            this.openDAL();
            ContentValues values = new ContentValues();
            values.put("nombre",cliente.getNombre());
            values.put("Apellido",cliente.getApellido());
            values.put("Correo",cliente.getCorreo());

            count = sql.insert("Clientes",null,values);

        }catch (Exception e){
            throw e;
        }finally {
            sql.close();
        }
        return count;
    }

    public psgs_Client selectByCodigoDAL(int codigo){
        psgs_Client cliente=null;
        try {
            this.openDAL();
            String select = "SELECT Codigo, Nombre, Apellido, Correo FROM Clientes WHERE Codigo="+codigo;
            Cursor cursor = sql.rawQuery(select, null);

            if(cursor.moveToFirst())
            {
                cliente = new psgs_Client();
                cliente.setNombre(cursor.getString(1));
                cliente.setApellido(cursor.getString(2));
                cliente.setCorreo(cursor.getString(3));
            }

        }catch (Exception e){
            throw e;
        }finally {
            sql.close();
        }
        return cliente;
    }

    public ArrayList<String> selectDAL(){
        ArrayList<String> list = null;
        try{
            this.openDAL();
            String select = "SELECT Codigo, Nombre, Apellido, Correo FROM Clientes";
            Cursor cursor = sql.rawQuery(select, null);

            if(cursor.moveToFirst())
            {
                list = new ArrayList<String>();
                do{
                    list.add(
                            cursor.getString(1)+" "+
                                    cursor.getString(2)+" "+
                                    cursor.getString(3)+" "+
                                    cursor.getString(4)
                    );
                }while (cursor.moveToNext());

            }

        }catch (Exception e){
            throw e;
        }finally {
            sql.close();
        }
        return list;
    }

    public ArrayList<psgs_Client> selectDAL2(){
        ArrayList<psgs_Client> list = null;
        try{
            this.openDAL();
            String select = "SELECT Codigo, Nombre, Apellido, Correo FROM Clientes";
            Cursor cursor = sql.rawQuery(select, null);

            if(cursor.moveToFirst())
            {
                list = new ArrayList<psgs_Client>();
                do{
                    psgs_Client cliente = new psgs_Client();
                    cliente.setCodigo(Integer.valueOf(cursor.getString(0)));
                    cliente.setNombre(cursor.getString(1));
                    cliente.setApellido(cursor.getString(2));
                    cliente.setCorreo(cursor.getString(3));
                    list.add(cliente);
                }while (cursor.moveToNext());

            }

        }catch (Exception e){
            throw e;
        }finally {
            sql.close();
        }
        return list;
    }

    public int deleteDAL(int codigo){
        int count =0;
        try {
            this.openDAL();
            count = sql.delete("Clientes","Codigo="+codigo,null);

        }catch (Exception e){
            throw e;
        }finally {
            sql.close();
        }
        return count;
    }

    public int updateDAL(psgs_Client cliente){
        int count =0;
        try{
            this.openDAL();
            ContentValues values = new ContentValues();
            values.put("Nombre",cliente.getNombre());
            values.put("Apellido",cliente.getApellido());
            values.put("Correo",cliente.getCorreo());
            count=sql.update("Clientes",values,"Codigo="+cliente.getCodigo(),null);
        }catch (Exception e){
            throw e;
        }finally {
            this.sql.close();
        }
        return count;
    }

}

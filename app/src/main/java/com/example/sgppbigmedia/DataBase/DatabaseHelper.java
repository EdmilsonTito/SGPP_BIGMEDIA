package com.example.sgppbigmedia.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sgppbigmedia.Campanha.Campanha;
import com.example.sgppbigmedia.Painel.Painel;
import com.example.sgppbigmedia.Tarefas.Tarefas;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context , "Sgppbigmedia.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Funcionario (idFunc integer primary key autoincrement, nome text,email text, Morada text, funcao text,bi text,foto blob)");
        db.execSQL("create table Tarefas (id_tarefa integer primary key autoincrement, descricao text, data text, estadoTarefa text, idfunc integer, FOREIGN KEY(idfunc) REFERENCES Funcionario(idFunc))");
        db.execSQL("create table Conta (id_conta integer primary key autoincrement, email text, senha text, idfunc integer, FOREIGN KEY(idfunc) REFERENCES Funcionario(idFunc))");
        db.execSQL("create table Localizacao (id_localizacao integer primary key autoincrement, descricao text, latitude real, longitude real,imgloc blob)");
        db.execSQL("create table Painel (id_painel integer primary key autoincrement, codPainel text, tipo text,largura text, altura text,cb text, idLoc integer,FOREIGN KEY(idLoc) REFERENCES Localizacao(id_Localizacao))");
        db.execSQL("create table Faces (id_faces integer primary key autoincrement, estadoUtilizacao text,codigoFace text,idpainel integer, FOREIGN KEY(idpainel) REFERENCES Painel(id_painel))");
        db.execSQL("create table Campanha (id_campanha integer primary key autoincrement, dataPublicacao text, duracao integer,imgcampanha blob, idfaces integer,FOREIGN KEY(idfaces) REFERENCES Faces(id_faces))");
        db.execSQL("create table Cliente (id_cliente integer primary key autoincrement, nomecliente text, tipoCliente, emailcliente text,morada text, idcampanha integer,FOREIGN KEY(idcampanha) REFERENCES Campanha(id_campanha))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Conta ");
        db.execSQL("drop table if exists Funcionario ");
        db.execSQL("drop table if exists Tarefas ");
        db.execSQL("drop table if exists Localizacao ");
        db.execSQL("drop table if exists Painel ");
        db.execSQL("drop table if exists Faces ");
        db.execSQL("drop table if exists Campanha ");
        db.execSQL("drop table if exists Cliente ");
    }


    //Validar o email e senha
    public Boolean emailsenha( String email, String senha){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Conta  where email =? and senha=?", new String[]{email,senha});

        if(cursor.getCount()>0) return true;
        else return false;
    }

    //Consultar o Funcionario
    public  Cursor consultafunc(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Funcionario where email =?", new String[]{email});
        return  cursor;
    }

    //Consultar senha
    public Boolean consultarsenha(String senhaAntiga, String Email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from Conta where senha=?",new String[]{senhaAntiga});

        if(c.getCount()>0){
            return true;
        }else return false;
    }
    //Alterar Senha
    public void Alteracaoasenha(String senhaAntiga, String senhaNova, String Email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update Conta set senha =? where  email=? and senha=?", new String[]{senhaNova,Email,senhaAntiga});
    }

    //Consultar Painel
   /* public ArrayList<Painel> getListaPainel(){
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery("select nome,latitude,longitude,descricao,dataPublicacao,estadoUtilizacao,duracao,codigoFace,cb,altura,largura,codPainel,tipo,imgcampanha,imgloc \n" +
               "from localizacao l inner join Painel p on l.id_localizacao=p.idLoc\n" +
               "inner join Faces f on p.id_painel=f.idpainel \n" +
               "inner join Campanha c on f.id_faces = c.idfaces\n" +
               "inner join Cliente cl on c.idCliente=cl.id_cliente",null);

       ArrayList<Painel> paineis = new ArrayList<Painel>();

        while(cursor.moveToNext()){
            Painel p = new Painel();
            p.setNomeCliente(cursor.getString(0));
            p.setLatitude(cursor.getDouble(1));
            p.setLongitude(cursor.getDouble(2));
            p.setDescricaoLoc(cursor.getString(3));
            p.setData_Pub(cursor.getString(4));
            p.setEstadoUtilizacao(cursor.getString(5));
            p.setTempo_Duracao(cursor.getString(6));
            p.setCodFace(cursor.getString(7));
            p.setCB(cursor.getString(8));
            p.setAltura(cursor.getString(9));
            p.setLargura(cursor.getString(10));
            p.setCodigoPainel(cursor.getString(11));
            p.setTipo_Painel(cursor.getString(12));

            paineis.add(p);
        }
        return paineis;
    }
*/

    //Consultar Campanha
    public ArrayList<Campanha> getListaCampanha(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct nome\n" +
                "from localizacao l inner join Painel p on l.id_localizacao=p.idLoc\n" +
                "inner join Faces f on p.id_painel=f.idpainel \n" +
                "inner join Campanha c on f.id_faces = c.idfaces\n" +
                "inner join Cliente cl on c.idCliente=cl.id_cliente",null);

        ArrayList<Campanha> cliente = new ArrayList<Campanha>();

        while(cursor.moveToNext()){
            Campanha campanha = new Campanha();
            campanha.setNomeCliente(cursor.getString(0));
            /*campanha.setData_Pub(cursor.getString(1));
            campanha.setTempo_Duracao(cursor.getString(2));
            campanha.setCodFace(cursor.getString(3));
            campanha.setCodigoPainel(cursor.getString(4));
            campanha.setTipo_Painel(cursor.getString(4));*/

            cliente.add(campanha);
        }
        return cliente;
    }


    //Consultar Tarefas
    public ArrayList<Tarefas> getListaTarefas(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select Data,Descricao,EstadoTarefa,Prioridade,Titulo \n" +
                "from Tarefas t inner join Funcionario f on t.idfunc=f.idFunc\n",null);

        ArrayList<Tarefas> tarefas = new ArrayList<Tarefas>();

        while(cursor.moveToNext()){
            Tarefas t = new Tarefas();
            t.setData(cursor.getString(0));
            t.setDescricao(cursor.getString(1));
            t.setEstadotarefa(cursor.getString(2));
            t.setPrioridade(cursor.getString(3));
            t.setTitulo(cursor.getString(4));
            tarefas.add(t);
        }
        return tarefas;
    }


    //Consultar os Paineis do Cliente
   /* public ArrayList<Painel> getListaPainelCliente( String nomecliente){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct nome,latitude,longitude,descricao,dataPublicacao,estadoUtilizacao,duracao,codigoFace,cb,altura,largura,codPainel,tipo,imgcampanha,imgloc \n" +
                       "from localizacao l inner join Painel p on l.id_localizacao=p.idLoc\n" +
                       "inner join Faces f on p.id_painel=f.idpainel\n" +
                       "inner join Campanha c on f.id_faces = c.idfaces\n" +
                       "inner join Cliente cl on c.idCliente=cl.id_cliente where cl.nome=?" ,new String[]{nomecliente});

        ArrayList<Painel> paineis = new ArrayList<Painel>();

        while(cursor.moveToNext()){
            Painel p = new Painel();
            p.setNomeCliente(cursor.getString(0));
            p.setLatitude(cursor.getDouble(1));
            p.setLongitude(cursor.getDouble(2));
            p.setDescricaoLoc(cursor.getString(3));
            p.setData_Pub(cursor.getString(4));
            p.setEstadoUtilizacao(cursor.getString(5));
            p.setTempo_Duracao(cursor.getString(6));
            p.setCodFace(cursor.getString(7));
            p.setCB(cursor.getString(8));
            p.setAltura(cursor.getString(9));
            p.setLargura(cursor.getString(10));
            p.setCodigoPainel(cursor.getString(11));
            p.setTipo_Painel(cursor.getString(12));

            paineis.add(p);
        }
        return paineis;
    }*/
}
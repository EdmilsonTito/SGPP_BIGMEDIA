package com.example.sgppbigmedia;

import androidx.annotation.NonNull;

public class Curso {
    private int Codigo;
    private String Nome;
    private String Categoria;
    private String Professor;

    public Curso() {
    }

    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getProfessor() {
        return Professor;
    }

    public void setProfessor(String professor) {
        Professor = professor;
    }

    @NonNull
    @Override
    public String toString() {
        return Nome;
    }
}

package com.example.sgppbigmedia.Tarefas;

import java.io.Serializable;

public class Tarefas implements Serializable {

        private Integer idTarefa;
        private String Data;
        private String Estadotarefa;
        private String Descricao;
        private String Titulo;
        private String Prioridade;
        private Integer idFunc;


    public Tarefas() {
    }

    public Integer getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(Integer idTarefa) {
        this.idTarefa = idTarefa;
    }

    public Integer getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(Integer idFunc) {
        this.idFunc = idFunc;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getEstadotarefa() {
        return Estadotarefa;
    }

    public void setEstadotarefa(String estadotarefa) {
        Estadotarefa = estadotarefa;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getPrioridade() {
        return Prioridade;
    }

    public void setPrioridade(String prioridade) {
        Prioridade = prioridade;
    }

    public String toString() {
        return  Titulo ;
    }
}
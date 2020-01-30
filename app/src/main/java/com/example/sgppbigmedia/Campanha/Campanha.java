package com.example.sgppbigmedia.Campanha;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.sql.Blob;

public class Campanha implements Serializable {

    private Integer idfaces;
    private Integer idCliente;
    private Integer idFunc;
    private String NomeCliente;
    private String Data_Pub;
    private String Tempo_Duracao;
    private String Tipo_Painel;
    private String CodFace;
    private String CodigoPainel;
    private Integer id_Faces;
    private Double Latitude;
    private Double Longitude;
    private String DescricaoLoc;
    private String EstadoUtilizacao;
    private String CB;
    private String Altura;
    private String Largura;
    private Blob imagLoc;
    private String imagLoc_url;
    private Blob imagCamp;
    private String imagCamp_url;

    public Campanha() {
    }

    public Integer getIdfaces() {
        return idfaces;
    }

    public void setIdfaces(Integer idfaces) {
        this.idfaces = idfaces;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(Integer idFunc) {
        this.idFunc = idFunc;
    }

    public Integer getId_Faces() {
        return id_Faces;
    }

    public void setId_Faces(Integer id_Faces) {
        this.id_Faces = id_Faces;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getDescricaoLoc() {
        return DescricaoLoc;
    }

    public void setDescricaoLoc(String descricaoLoc) {
        DescricaoLoc = descricaoLoc;
    }

    public String getEstadoUtilizacao() {
        return EstadoUtilizacao;
    }

    public void setEstadoUtilizacao(String estadoUtilizacao) {
        EstadoUtilizacao = estadoUtilizacao;
    }

    public String getCB() {
        return CB;
    }

    public void setCB(String CB) {
        this.CB = CB;
    }

    public String getAltura() {
        return Altura;
    }

    public void setAltura(String altura) {
        Altura = altura;
    }

    public String getLargura() {
        return Largura;
    }

    public void setLargura(String largura) {
        Largura = largura;
    }

    public Blob getImagLoc() {
        return imagLoc;
    }

    public void setImagLoc(Blob imagLoc) {
        this.imagLoc = imagLoc;
    }

    public String getImagLoc_url() {
        return imagLoc_url;
    }

    public void setImagLoc_url(String imagLoc_url) {
        this.imagLoc_url = imagLoc_url;
    }

    public Blob getImagCamp() {
        return imagCamp;
    }

    public void setImagCamp(Blob imagCamp) {
        this.imagCamp = imagCamp;
    }

    public String getImagCamp_url() {
        return imagCamp_url;
    }

    public void setImagCamp_url(String imagCamp_url) {
        this.imagCamp_url = imagCamp_url;
    }

    public String getNomeCliente() {
        return NomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        NomeCliente = nomeCliente;
    }

    public String getData_Pub() {
        return Data_Pub;
    }

    public void setData_Pub(String data_Pub) {
        Data_Pub = data_Pub;
    }

    public String getTempo_Duracao() {
        return Tempo_Duracao;
    }

    public void setTempo_Duracao(String tempo_Duracao) {
        Tempo_Duracao = tempo_Duracao;
    }

    public String getTipo_Painel() {
        return Tipo_Painel;
    }

    public void setTipo_Painel(String tipo_Painel) {
        Tipo_Painel = tipo_Painel;
    }

    public String getCodFace() {
        return CodFace;
    }

    public void setCodFace(String codFace) {
        CodFace = codFace;
    }

    public String getCodigoPainel() {
        return CodigoPainel;
    }

    public void setCodigoPainel(String codigoPainel) {
        CodigoPainel = codigoPainel;
    }

    @NonNull
    @Override
    public String toString() {
        return NomeCliente;
    }
}

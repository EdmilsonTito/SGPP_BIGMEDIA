package com.example.sgppbigmedia.Painel;

import java.io.Serializable;
import java.sql.Blob;

public class Painel  implements Serializable {
    private Integer idCampanha;
    private Integer idfaces;
    private Integer idCliente;
    private Integer idFunc;
    private Double Latitude;
    private Double Longitude;
    private String DescricaoLoc;
    private String EstadoUtilizacao;
    private String Tipo_Painel;
    private String CodFace;
    private String CB;
    private String Altura;
    private String Largura;
    private String CodigoPainel;
    private Blob imagLoc;
    private String imagLoc_url;
    private String NomeCliente;
    private String Data_Pub;
    private String imagCamp_url;
    private String Tempo_Duracao;



    public Painel() {
    }

    public Integer getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(Integer idCampanha) {
        this.idCampanha = idCampanha;
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

    public String getTempo_Duracao() {
        return Tempo_Duracao;
    }

    public void setTempo_Duracao(String tempo_Duracao) {
        Tempo_Duracao = tempo_Duracao;
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

    public String getImagLoc_url() {
        return imagLoc_url;
    }

    public void setImagLoc_url(String imagLoc_url) {
        this.imagLoc_url = imagLoc_url;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public void setDescricaoLoc(String descricaoLoc) {
        DescricaoLoc = descricaoLoc;
    }

    public void setEstadoUtilizacao(String estadoUtilizacao) {
        EstadoUtilizacao = estadoUtilizacao;
    }

    public void setTipo_Painel(String tipo_Painel) {
        Tipo_Painel = tipo_Painel;
    }

    public void setCodFace(String codFace) {
        CodFace = codFace;
    }

    public void setCB(String CB) {
        this.CB = CB;
    }

    public void setAltura(String altura) {
        Altura = altura;
    }

    public void setLargura(String largura) {
        Largura = largura;
    }

    public void setCodigoPainel(String codigoPainel) {
        CodigoPainel = codigoPainel;
    }

    public void setImgLoc(Blob imgLoc) {
        this.imagLoc = imgLoc;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getDescricaoLoc() {
        return DescricaoLoc;
    }

    public String getEstadoUtilizacao() {
        return EstadoUtilizacao;
    }

    public String getTipo_Painel() {
        return Tipo_Painel;
    }

    public String getCodFace() {
        return CodFace;
    }

    public String getCB() {
        return CB;
    }

    public String getAltura() {
        return Altura;
    }

    public String getLargura() {
        return Largura;
    }

    public String getCodigoPainel() {
        return CodigoPainel;
    }

    public Blob getImgLoc() {
        return imagLoc;
    }

    @Override
    public String toString() {
       return  CodigoPainel + '-'+ CodFace ;
    }
}

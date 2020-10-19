package com.skripsi.cuanku.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;


public class Tabungan {
    private String namatarget;
    private String durasitarget;
    private String nominal;
    private String tanggal;
    private String nama;
    private String tabunganharian;
    private String tabunganbulanan;




    public String getTabunganharian() {
        return tabunganharian;
    }

    public void setTabunganharian(String tabunganharian) {
        this.tabunganharian = tabunganharian;
    }

    public String getTabunganbulanan() {
        return tabunganbulanan;
    }

    public void setTabunganbulanan(String tabunganbulanan) {
        this.tabunganbulanan = tabunganbulanan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamatarget() {
        return namatarget;
    }

    public void setNamatarget(String namatarget) {
        this.namatarget = namatarget;
    }

    public String getDurasitarget() {
        return durasitarget;
    }

    public void setDurasitarget(String durasitarget) {
        this.durasitarget = durasitarget;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }


    public Tabungan(String durasitarget, String namatarget, String nominal, String nama) {
        this.durasitarget = durasitarget;
        this.nama = nama;
        this.namatarget = namatarget;
        this.nominal = nominal;
    }

    public Tabungan() {
    }

    public Tabungan(String nominal) {
        this.nominal = nominal;

    }

}

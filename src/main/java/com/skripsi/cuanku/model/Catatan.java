package com.skripsi.cuanku.model;


import java.io.Serializable;

public class Catatan implements Serializable {
    private String keterangan;
    private String tanggal;
    private String jumlah;
    private String deposit;
    private String tipe;
    private String nama;
    private String hasil;

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Catatan() {
    }

    public Catatan(String keterangan, String tanggal, String tipe, String jumlah, String deposit, String nama) {
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.deposit = deposit;
        this.nama = nama;
    }

    public Catatan(String hasil, String nama) {
        this.hasil = hasil;
        this.nama = nama;
    }
}

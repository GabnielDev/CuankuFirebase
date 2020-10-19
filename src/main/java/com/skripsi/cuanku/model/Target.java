package com.skripsi.cuanku.model;

public class Target {
    private String sisatarget;
    private String namatarget;
    private String nominaltarget;
    private String durasitarget;
    private String hariantarget;
    private String bulanantarget;
    private String nama;


    public Target() {
    }

    public String getSisatarget() {
        return sisatarget;
    }

    public void setSisatarget(String sisatarget) {
        this.sisatarget = sisatarget;
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

    public String getNominaltarget() {
        return nominaltarget;
    }

    public void setNominaltarget(String nominaltarget) {
        this.nominaltarget = nominaltarget;
    }

    public String getDurasitarget() {
        return durasitarget;
    }

    public void setDurasitarget(String durasitarget) {
        this.durasitarget = durasitarget;
    }

    public String getHariantarget() {
        return hariantarget;
    }

    public void setHariantarget(String hariantarget) {
        this.hariantarget = hariantarget;
    }

    public String getBulanantarget() {
        return bulanantarget;
    }

    public void setBulanantarget(String bulanantarget) {
        this.bulanantarget = bulanantarget;
    }

    public Target(String namatarget, String nominaltarget, String durasitarget, String hariantarget, String bulanantarget, String nama, String sisatarget) {
        this.namatarget = namatarget;
        this.nominaltarget = nominaltarget;
        this.durasitarget = durasitarget;
        this.hariantarget = hariantarget;
        this.bulanantarget = bulanantarget;
        this.nama = nama;
        this.sisatarget = sisatarget;
    }

    public Target(String namatarget, String nominaltarget, String durasitarget, String hariantarget) {
        this.namatarget = namatarget;
        this.nominaltarget = nominaltarget;
        this.durasitarget = durasitarget;
        this.hariantarget = hariantarget;
    }
}

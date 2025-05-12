package com.example.springboot.entity;

public class Workshop {
    private Integer id;
    private String workshopName;  // 车间名称
    private String workshopTeam;  // 车间班组

    @Override
    public String toString() {
        return "Workshop{" +
                "id=" + id +
                ", workshopName='" + workshopName + '\'' +
                ", workshopTeam='" + workshopTeam + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getWorkshopTeam() {
        return workshopTeam;
    }

    public void setWorkshopTeam(String workshopTeam) {
        this.workshopTeam = workshopTeam;
    }
}
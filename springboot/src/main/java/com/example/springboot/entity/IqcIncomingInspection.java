package com.example.springboot.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IqcIncomingInspection {
    private Integer id;
    private String inspector;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inspectionDate;
    private String material;
    private String supplier;
    private Integer inspectionQuantity;
    private Integer defectiveQuantity;
    private String defectiveRate;
    private InspectionResult inspectionResult;
    private String remarks;

    @Override
    public String toString() {
        return "IqcIncomingInspection{" +
                "id=" + id +
                ", inspector='" + inspector + '\'' +
                ", inspectionDate=" + inspectionDate +
                ", material='" + material + '\'' +
                ", supplier='" + supplier + '\'' +
                ", inspectionQuantity=" + inspectionQuantity +
                ", defectiveQuantity=" + defectiveQuantity +
                ", defectiveRate=" + defectiveRate + '\'' +
                ", inspectionResult='" + inspectionResult + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getInspectionQuantity() {
        return inspectionQuantity;
    }

    public void setInspectionQuantity(Integer inspectionQuantity) {
        this.inspectionQuantity = inspectionQuantity;
    }

    public Integer getDefectiveQuantity() {
        return defectiveQuantity;
    }

    public void setDefectiveQuantity(Integer defectiveQuantity) {
        this.defectiveQuantity = defectiveQuantity;
    }

    public String getDefectiveRate() {
        return defectiveRate;
    }

    public void setDefectiveRate(String defectiveRate) {
        this.defectiveRate = defectiveRate;
    }

    public InspectionResult getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = InspectionResult.valueOf(inspectionResult);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
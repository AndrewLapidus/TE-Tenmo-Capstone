package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    private int transferIid;
    private BigDecimal amount;
    private String loggedIn;
    private String recipiant;



    public TransferDTO(){}

    public TransferDTO(int transferIid, BigDecimal amount, String loggedIn, String recipiant) {
        this.transferIid = transferIid;
        this.amount = amount;
        this.loggedIn = loggedIn;
        this.recipiant = recipiant;
    }

    public int getTransferIid() {
        return transferIid;
    }

    public void setTransferIid(int transferIid) {
        this.transferIid = transferIid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(String loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getRecipiant() {
        return recipiant;
    }

    public void setRecipiant(String recipiant) {
        this.recipiant = recipiant;
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "transferid=" + transferIid +
                ", transferAmount='" + amount + '\'' +
                ", from=" + loggedIn +
                ", to=" + recipiant +
                '}';
    }



}

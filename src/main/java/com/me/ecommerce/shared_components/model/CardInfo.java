package com.me.ecommerce.shared_components.model;

public class CardInfo {
    private String cardName;
    private String cardNo;
    private String cvv;
    private String exp;

    public CardInfo() {
    }

    public CardInfo(String cardName, String cardNo, String cvv, String exp) {
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.cvv = cvv;
        this.exp = exp;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}

package com.joca.model.registration.payment;

public class Payment {
    private PaymentMethodEnum method;
    private double amount;

    public Payment(PaymentMethodEnum method, double amount) {
        this.method = method;
        this.amount = amount;
    }

    public Payment() {
    }

    public PaymentMethodEnum getMethod() {
        return method;
    }

    public void setMethod(PaymentMethodEnum method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

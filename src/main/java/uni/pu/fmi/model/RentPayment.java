package uni.pu.fmi.model;

import java.time.LocalDateTime;

public class RentPayment {
    private int id;
    private User student;
    private Room room;
    private double amount;
    private String period;
    private PaymentStatus status;
    private LocalDateTime paidAt;

    public RentPayment() {
    }

    public RentPayment(int id, User student, Room room, double amount, String period) {
        this.id = id;
        this.student = student;
        this.room = room;
        this.amount = amount;
        this.period = period;
        this.status = PaymentStatus.PENDING;
    }

    public boolean isPaid() {
        return status == PaymentStatus.PAID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}

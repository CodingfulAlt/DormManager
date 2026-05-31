package uni.pu.fmi.model;

import java.time.LocalDateTime;

public class Application {
    private int id;
    private User student;
    private Room room;
    private RoomType type;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private String notes;

    public Application() {
    }

    public Application(int id, User student, RoomType type) {
        this.id = id;
        this.student = student;
        this.type = type;
        this.status = ApplicationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void approve(Room room) {
        this.room = room;
        this.status = ApplicationStatus.APPROVED;
    }

    public void reject(String reason) {
        this.status = ApplicationStatus.REJECTED;
        this.notes = reason;
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

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

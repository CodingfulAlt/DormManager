package uni.pu.fmi.model;

public class Room {
    private int id;
    private String number;
    private RoomType type;
    private int floor;
    private double monthlyPrice;
    private RoomStatus status;

    public Room() {
    }

    public Room(int id, String number, RoomType type, int floor, double monthlyPrice, RoomStatus status) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.floor = floor;
        this.monthlyPrice = monthlyPrice;
        this.status = status;
    }

    public boolean isFree() {
        return status == RoomStatus.FREE;
    }

    public void occupy() {
        this.status = RoomStatus.OCCUPIED;
    }

    public void release() {
        this.status = RoomStatus.FREE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}

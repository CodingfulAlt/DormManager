package uni.pu.fmi.repo;

import uni.pu.fmi.model.Room;
import uni.pu.fmi.model.RoomStatus;
import uni.pu.fmi.model.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomRepo {
    private final List<Room> rooms = new ArrayList<>();
    private int nextId = 1;

    public Room save(Room room) {
        if (room.getId() == 0) {
            room.setId(nextId++);
        }
        rooms.add(room);
        return room;
    }

    public Optional<Room> findById(int id) {
        return rooms.stream().filter(r -> r.getId() == id).findFirst();
    }

    public List<Room> findFreeRooms() {
        return rooms.stream()
                .filter(Room::isFree)
                .collect(Collectors.toList());
    }

    public List<Room> findFreeRoomsByType(RoomType type) {
        return rooms.stream()
                .filter(Room::isFree)
                .filter(r -> r.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms);
    }

    public void clear() {
        rooms.clear();
        nextId = 1;
    }
}

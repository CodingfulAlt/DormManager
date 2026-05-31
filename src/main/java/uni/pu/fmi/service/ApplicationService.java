package uni.pu.fmi.service;

import uni.pu.fmi.exception.BusinessException;
import uni.pu.fmi.model.Application;
import uni.pu.fmi.model.ApplicationStatus;
import uni.pu.fmi.model.Role;
import uni.pu.fmi.model.Room;
import uni.pu.fmi.model.RoomType;
import uni.pu.fmi.model.User;
import uni.pu.fmi.repo.ApplicationRepo;
import uni.pu.fmi.repo.RoomRepo;

import java.util.List;

public class ApplicationService {

    private final ApplicationRepo applicationRepo;
    private final RoomRepo roomRepo;

    public ApplicationService(ApplicationRepo applicationRepo, RoomRepo roomRepo) {
        this.applicationRepo = applicationRepo;
        this.roomRepo = roomRepo;
    }

    // UC2 - Кандидатстване за стая
    public Application apply(User student, RoomType type) {
        if (student == null) {
            throw new BusinessException("Не сте влезли в системата");
        }
        if (student.getRole() != Role.STUDENT) {
            throw new BusinessException("Само студент може да кандидатства за стая");
        }
        if (type == null) {
            throw new BusinessException("Невалиден тип стая");
        }
        if (applicationRepo.findActiveByStudent(student).isPresent()) {
            throw new BusinessException("Вече имате активна кандидатура");
        }
        Application application = new Application(0, student, type);
        return applicationRepo.save(application);
    }

    // extend на UC2 - Преглед на свободни стаи
    public List<Room> viewFreeRooms(RoomType type) {
        return roomRepo.findFreeRoomsByType(type);
    }

    public Application getStatus(User student) {
        return applicationRepo.findActiveByStudent(student)
                .orElseThrow(() -> new BusinessException("Нямате активна кандидатура"));
    }

    // UC6 - Одобряване на кандидатура (само админ)
    public Application approve(User admin, int applicationId) {
        requireAdmin(admin);
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new BusinessException("Кандидатурата не съществува"));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessException("Кандидатурата вече е обработена");
        }

        List<Room> freeRooms = roomRepo.findFreeRoomsByType(application.getType());
        if (freeRooms.isEmpty()) {
            throw new BusinessException("Няма свободни стаи от желания тип");
        }

        // include - Настаняване в стая
        Room room = freeRooms.get(0);
        room.occupy();
        application.approve(room);
        return application;
    }

    // UC6 - Отхвърляне на кандидатура (само админ)
    public Application reject(User admin, int applicationId, String reason) {
        requireAdmin(admin);
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new BusinessException("Кандидатурата не съществува"));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessException("Кандидатурата вече е обработена");
        }

        application.reject(reason);
        return application;
    }

    private void requireAdmin(User user) {
        if (user == null || user.getRole() != Role.ADMIN) {
            throw new BusinessException("Само администратор може да обработва кандидатури");
        }
    }
}

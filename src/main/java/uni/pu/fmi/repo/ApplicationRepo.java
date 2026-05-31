package uni.pu.fmi.repo;

import uni.pu.fmi.model.Application;
import uni.pu.fmi.model.ApplicationStatus;
import uni.pu.fmi.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApplicationRepo {
    private final List<Application> applications = new ArrayList<>();
    private int nextId = 1;

    public Application save(Application application) {
        if (application.getId() == 0) {
            application.setId(nextId++);
        }
        applications.add(application);
        return application;
    }

    public Optional<Application> findById(int id) {
        return applications.stream().filter(a -> a.getId() == id).findFirst();
    }

    public Optional<Application> findActiveByStudent(User student) {
        return applications.stream()
                .filter(a -> a.getStudent().getId() == student.getId())
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING
                        || a.getStatus() == ApplicationStatus.APPROVED)
                .findFirst();
    }

    public List<Application> findByStatus(ApplicationStatus status) {
        return applications.stream()
                .filter(a -> a.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Application> findAll() {
        return new ArrayList<>(applications);
    }

    public void clear() {
        applications.clear();
        nextId = 1;
    }
}

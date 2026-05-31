package uni.pu.fmi.repo;

import uni.pu.fmi.model.PaymentStatus;
import uni.pu.fmi.model.RentPayment;
import uni.pu.fmi.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentRepo {
    private final List<RentPayment> payments = new ArrayList<>();
    private int nextId = 1;

    public RentPayment save(RentPayment payment) {
        if (payment.getId() == 0) {
            payment.setId(nextId++);
        }
        payments.add(payment);
        return payment;
    }

    public Optional<RentPayment> findByStudentAndPeriod(User student, String period) {
        return payments.stream()
                .filter(p -> p.getStudent().getId() == student.getId())
                .filter(p -> p.getPeriod().equals(period))
                .findFirst();
    }

    public List<RentPayment> findPaidByStudentAndPeriod(User student, String period) {
        return payments.stream()
                .filter(p -> p.getStudent().getId() == student.getId())
                .filter(p -> p.getPeriod().equals(period))
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .collect(Collectors.toList());
    }

    public List<RentPayment> findByStudent(User student) {
        return payments.stream()
                .filter(p -> p.getStudent().getId() == student.getId())
                .collect(Collectors.toList());
    }

    public List<RentPayment> findAll() {
        return new ArrayList<>(payments);
    }

    public void clear() {
        payments.clear();
        nextId = 1;
    }
}

package uni.pu.fmi.service;

import uni.pu.fmi.exception.BusinessException;
import uni.pu.fmi.model.PaymentStatus;
import uni.pu.fmi.model.RentPayment;
import uni.pu.fmi.model.Role;
import uni.pu.fmi.model.Room;
import uni.pu.fmi.model.User;
import uni.pu.fmi.repo.PaymentRepo;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentService {

    private final PaymentRepo paymentRepo;

    public PaymentService(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    // UC4 - Плащане на наем
    public RentPayment payRent(User student, Room room, String period, double amount) {
        if (student == null) {
            throw new BusinessException("Не сте влезли в системата");
        }
        if (student.getRole() != Role.STUDENT) {
            throw new BusinessException("Само студент може да плаща наем");
        }
        if (room == null) {
            throw new BusinessException("Не сте настанен в стая");
        }
        if (!paymentRepo.findPaidByStudentAndPeriod(student, period).isEmpty()) {
            throw new BusinessException("Наемът вече е платен за този период");
        }
        if (amount < room.getMonthlyPrice()) {
            throw new BusinessException("Недостатъчна сума за плащане на наема");
        }

        RentPayment payment = new RentPayment(0, student, room, room.getMonthlyPrice(), period);
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        return paymentRepo.save(payment);
    }

    // extend на UC4 - Преглед на история на плащания
    public List<RentPayment> getHistory(User student) {
        if (student == null) {
            throw new BusinessException("Не сте влезли в системата");
        }
        return paymentRepo.findByStudent(student);
    }
}

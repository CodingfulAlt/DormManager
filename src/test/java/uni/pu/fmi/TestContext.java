package uni.pu.fmi;

import uni.pu.fmi.model.Application;
import uni.pu.fmi.model.RentPayment;
import uni.pu.fmi.model.Room;
import uni.pu.fmi.model.User;
import uni.pu.fmi.repo.ApplicationRepo;
import uni.pu.fmi.repo.PaymentRepo;
import uni.pu.fmi.repo.RoomRepo;
import uni.pu.fmi.repo.UserRepo;
import uni.pu.fmi.service.ApplicationService;
import uni.pu.fmi.service.PaymentService;

import java.util.List;

/**
 * Споделен контекст между step класовете. Cucumber (чрез PicoContainer)
 * създава една инстанция за сценарий и я подава на всички step класове,
 * така че те споделят едни и същи repo-та, services и състояние.
 */
public class TestContext {

    // mock хранилища (in-memory) + services
    public final UserRepo userRepo = new UserRepo();
    public final RoomRepo roomRepo = new RoomRepo();
    public final ApplicationRepo applicationRepo = new ApplicationRepo();
    public final PaymentRepo paymentRepo = new PaymentRepo();

    public final ApplicationService applicationService =
            new ApplicationService(applicationRepo, roomRepo);
    public final PaymentService paymentService =
            new PaymentService(paymentRepo);

    // състояние на сценария
    public User actor;
    public Room room;
    public Application application;
    public RentPayment payment;
    public List<Room> freeRooms;
    public List<RentPayment> history;
    public String error;
}

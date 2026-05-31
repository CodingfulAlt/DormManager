package uni.pu.fmi;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uni.pu.fmi.exception.BusinessException;
import uni.pu.fmi.model.Application;
import uni.pu.fmi.model.ApplicationStatus;
import uni.pu.fmi.model.PaymentStatus;
import uni.pu.fmi.model.RentPayment;
import uni.pu.fmi.model.Role;
import uni.pu.fmi.model.Room;
import uni.pu.fmi.model.RoomStatus;
import uni.pu.fmi.model.RoomType;
import uni.pu.fmi.model.User;
import uni.pu.fmi.repo.ApplicationRepo;
import uni.pu.fmi.repo.PaymentRepo;
import uni.pu.fmi.repo.RoomRepo;
import uni.pu.fmi.repo.UserRepo;
import uni.pu.fmi.service.ApplicationService;
import uni.pu.fmi.service.PaymentService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DormManagerSteps {

    // --- Repos & services (mock in-memory) ---
    private UserRepo userRepo;
    private RoomRepo roomRepo;
    private ApplicationRepo applicationRepo;
    private PaymentRepo paymentRepo;
    private ApplicationService applicationService;
    private PaymentService paymentService;

    // --- Scenario state ---
    private User actor;
    private Room room;
    private Application application;
    private RentPayment payment;
    private List<Room> freeRooms;
    private List<RentPayment> history;
    private String error;

    @Before
    public void setUp() {
        userRepo = new UserRepo();
        roomRepo = new RoomRepo();
        applicationRepo = new ApplicationRepo();
        paymentRepo = new PaymentRepo();
        applicationService = new ApplicationService(applicationRepo, roomRepo);
        paymentService = new PaymentService(paymentRepo);

        actor = null;
        room = null;
        application = null;
        payment = null;
        freeRooms = null;
        history = null;
        error = null;
    }

    // =========================================================
    //  ОБЩИ СТЪПКИ (актьори, стаи, грешки)
    // =========================================================

    @Given("студент с имейл {string}")
    public void студентСИмейл(String email) {
        actor = new User(0, "Студент", email, "hash", Role.STUDENT);
    }

    @Given("администратор с имейл {string}")
    public void администраторСИмейл(String email) {
        actor = new User(0, "Админ", email, "hash", Role.ADMIN);
    }

    @Given("в системата има свободна стая от тип {string}")
    public void имаСвободнаСтая(String type) {
        roomRepo.save(new Room(0, "101", RoomType.valueOf(type), 1, 150.0, RoomStatus.FREE));
    }

    @Given("в системата има {int} свободни стаи от тип {string}")
    public void имаНСвободниСтаи(int count, String type) {
        for (int i = 1; i <= count; i++) {
            roomRepo.save(new Room(0, "20" + i, RoomType.valueOf(type), 2, 200.0, RoomStatus.FREE));
        }
    }

    @Then("грешката е {string}")
    public void грешкатаЕ(String expected) {
        assertEquals(expected, error);
    }

    // =========================================================
    //  КАНДИДАТСТВАНЕ ЗА СТАЯ (UC2)
    // =========================================================

    @Given("студентът вече има активна кандидатура")
    public void студентътВечеИмаКандидатура() {
        applicationService.apply(actor, RoomType.SINGLE);
    }

    @When("студентът кандидатства за стая от тип {string}")
    public void студентътКандидатства(String type) {
        applyFor(RoomType.valueOf(type));
    }

    @When("потребителят кандидатства за стая от тип {string}")
    public void потребителятКандидатства(String type) {
        applyFor(RoomType.valueOf(type));
    }

    @When("студентът кандидатства без да избере тип стая")
    public void студентътКандидатстваБезТип() {
        applyFor(null);
    }

    private void applyFor(RoomType type) {
        try {
            application = applicationService.apply(actor, type);
        } catch (BusinessException e) {
            error = e.getMessage();
        }
    }

    @When("студентът прегледа свободните стаи от тип {string}")
    public void студентътПрегледаСвободниСтаи(String type) {
        freeRooms = applicationService.viewFreeRooms(RoomType.valueOf(type));
    }

    @Then("кандидатурата е успешно подадена")
    public void кандидатуратаУспешна() {
        assertNotNull(application);
        assertNull(error);
    }

    @Then("статусът на кандидатурата е {string}")
    public void статусътНаКандидатурата(String status) {
        assertEquals(ApplicationStatus.valueOf(status), application.getStatus());
    }

    @Then("се показват {int} свободни стаи")
    public void сеПоказватСтаи(int count) {
        assertNotNull(freeRooms);
        assertEquals(count, freeRooms.size());
    }

    @Then("кандидатурата е неуспешна")
    public void кандидатуратаНеуспешна() {
        assertNotNull(error);
    }

    // =========================================================
    //  ОДОБРЯВАНЕ / ОТХВЪРЛЯНЕ (UC6)
    // =========================================================

    @Given("има подадена кандидатура от студент за тип {string}")
    public void имаПодаденаКандидатура(String type) {
        User student = new User(0, "Кандидат", "kandidat@uni.bg", "hash", Role.STUDENT);
        application = applicationService.apply(student, RoomType.valueOf(type));
    }

    @Given("кандидатурата вече е одобрена")
    public void кандидатуратаВечеОдобрена() {
        applicationService.approve(actor, application.getId());
    }

    @When("администраторът одобри кандидатурата")
    public void администраторътОдобри() {
        approveAs(actor);
    }

    @When("потребителят се опита да одобри кандидатурата")
    public void потребителятСеОпитаДаОдобри() {
        approveAs(actor);
    }

    private void approveAs(User user) {
        try {
            application = applicationService.approve(user, application.getId());
        } catch (BusinessException e) {
            error = e.getMessage();
        }
    }

    @When("администраторът отхвърли кандидатурата с причина {string}")
    public void администраторътОтхвърли(String reason) {
        try {
            application = applicationService.reject(actor, application.getId(), reason);
        } catch (BusinessException e) {
            error = e.getMessage();
        }
    }

    @Then("кандидатурата е със статус {string}")
    public void кандидатуратаСъсСтатус(String status) {
        assertNotNull(application);
        assertEquals(ApplicationStatus.valueOf(status), application.getStatus());
    }

    @Then("на студента е назначена стая")
    public void наСтудентаНазначенаСтая() {
        assertNotNull(application.getRoom());
    }

    @Then("стаята е със статус {string}")
    public void стаятаСъсСтатус(String status) {
        assertEquals(RoomStatus.valueOf(status), application.getRoom().getStatus());
    }

    @Then("одобряването е неуспешно")
    public void одобряванетоНеуспешно() {
        assertNotNull(error);
    }

    // =========================================================
    //  ПЛАЩАНЕ НА НАЕМ (UC4)
    // =========================================================

    @Given("настанен студент с имейл {string} в стая с наем {double}")
    public void настаненСтудент(String email, double rent) {
        actor = new User(0, "Студент", email, "hash", Role.STUDENT);
        room = new Room(0, "101", RoomType.SINGLE, 1, rent, RoomStatus.OCCUPIED);
    }

    @Given("студент с имейл {string} който не е настанен в стая")
    public void студентБезСтая(String email) {
        actor = new User(0, "Студент", email, "hash", Role.STUDENT);
        room = null;
    }

    @Given("студентът е платил наем за период {string}")
    public void студентътЕПлатил(String period) {
        paymentService.payRent(actor, room, period, room.getMonthlyPrice());
    }

    @Given("студентът вече е платил наем за период {string}")
    public void студентътВечеЕПлатил(String period) {
        paymentService.payRent(actor, room, period, room.getMonthlyPrice());
    }

    @When("студентът плати наем за период {string} със сума {double}")
    public void студентътПлати(String period, double amount) {
        try {
            payment = paymentService.payRent(actor, room, period, amount);
        } catch (BusinessException e) {
            error = e.getMessage();
        }
    }

    @When("студентът прегледа историята на плащанията")
    public void студентътПрегледаИстория() {
        history = paymentService.getHistory(actor);
    }

    @Then("плащането е успешно")
    public void плащанетоУспешно() {
        assertNotNull(payment);
        assertNull(error);
    }

    @Then("статусът на плащането е {string}")
    public void статусътНаПлащането(String status) {
        assertEquals(PaymentStatus.valueOf(status), payment.getStatus());
    }

    @Then("се показват {int} плащания")
    public void сеПоказватПлащания(int count) {
        assertNotNull(history);
        assertEquals(count, history.size());
    }

    @Then("плащането е неуспешно")
    public void плащанетоНеуспешно() {
        assertNotNull(error);
    }
}

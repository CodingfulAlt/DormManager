package uni.pu.fmi;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uni.pu.fmi.exception.BusinessException;
import uni.pu.fmi.model.PaymentStatus;
import uni.pu.fmi.model.Role;
import uni.pu.fmi.model.Room;
import uni.pu.fmi.model.RoomStatus;
import uni.pu.fmi.model.RoomType;
import uni.pu.fmi.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Стъпки за функционалност UC4 - Плащане на наем.
 */
public class PaymentSteps {

    private final TestContext ctx;

    public PaymentSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("настанен студент с имейл {string} в стая с наем {double}")
    public void настаненСтудент(String email, double rent) {
        ctx.actor = new User(0, "Студент", email, "hash", Role.STUDENT);
        ctx.room = new Room(0, "101", RoomType.SINGLE, 1, rent, RoomStatus.OCCUPIED);
    }

    @Given("студент с имейл {string} който не е настанен в стая")
    public void студентБезСтая(String email) {
        ctx.actor = new User(0, "Студент", email, "hash", Role.STUDENT);
        ctx.room = null;
    }

    @Given("студентът е платил наем за период {string}")
    public void студентътЕПлатил(String period) {
        ctx.paymentService.payRent(ctx.actor, ctx.room, period, ctx.room.getMonthlyPrice());
    }

    @Given("студентът вече е платил наем за период {string}")
    public void студентътВечеЕПлатил(String period) {
        ctx.paymentService.payRent(ctx.actor, ctx.room, period, ctx.room.getMonthlyPrice());
    }

    @When("студентът плати наем за период {string} със сума {double}")
    public void студентътПлати(String period, double amount) {
        try {
            ctx.payment = ctx.paymentService.payRent(ctx.actor, ctx.room, period, amount);
        } catch (BusinessException e) {
            ctx.error = e.getMessage();
        }
    }

    @When("студентът прегледа историята на плащанията")
    public void студентътПрегледаИстория() {
        ctx.history = ctx.paymentService.getHistory(ctx.actor);
    }

    @Then("плащането е успешно")
    public void плащанетоУспешно() {
        assertNotNull(ctx.payment);
        assertNull(ctx.error);
    }

    @Then("статусът на плащането е {string}")
    public void статусътНаПлащането(String status) {
        assertEquals(PaymentStatus.valueOf(status), ctx.payment.getStatus());
    }

    @Then("се показват {int} плащания")
    public void сеПоказватПлащания(int count) {
        assertNotNull(ctx.history);
        assertEquals(count, ctx.history.size());
    }

    @Then("плащането е неуспешно")
    public void плащанетоНеуспешно() {
        assertNotNull(ctx.error);
    }
}

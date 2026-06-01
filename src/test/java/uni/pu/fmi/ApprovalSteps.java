package uni.pu.fmi;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uni.pu.fmi.exception.BusinessException;
import uni.pu.fmi.model.ApplicationStatus;
import uni.pu.fmi.model.Role;
import uni.pu.fmi.model.RoomStatus;
import uni.pu.fmi.model.RoomType;
import uni.pu.fmi.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Стъпки за функционалност UC6 - Одобряване / отхвърляне на кандидатура.
 */
public class ApprovalSteps {

    private final TestContext ctx;

    public ApprovalSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("има подадена кандидатура от студент за тип {string}")
    public void имаПодаденаКандидатура(String type) {
        User student = new User(0, "Кандидат", "kandidat@uni.bg", "hash", Role.STUDENT);
        ctx.application = ctx.applicationService.apply(student, RoomType.valueOf(type));
    }

    @Given("кандидатурата вече е одобрена")
    public void кандидатуратаВечеОдобрена() {
        ctx.applicationService.approve(ctx.actor, ctx.application.getId());
    }

    @When("администраторът одобри кандидатурата")
    public void администраторътОдобри() {
        approveAs(ctx.actor);
    }

    @When("потребителят се опита да одобри кандидатурата")
    public void потребителятСеОпитаДаОдобри() {
        approveAs(ctx.actor);
    }

    private void approveAs(User user) {
        try {
            ctx.application = ctx.applicationService.approve(user, ctx.application.getId());
        } catch (BusinessException e) {
            ctx.error = e.getMessage();
        }
    }

    @When("администраторът отхвърли кандидатурата с причина {string}")
    public void администраторътОтхвърли(String reason) {
        try {
            ctx.application = ctx.applicationService.reject(ctx.actor, ctx.application.getId(), reason);
        } catch (BusinessException e) {
            ctx.error = e.getMessage();
        }
    }

    @Then("кандидатурата е със статус {string}")
    public void кандидатуратаСъсСтатус(String status) {
        assertNotNull(ctx.application);
        assertEquals(ApplicationStatus.valueOf(status), ctx.application.getStatus());
    }

    @Then("на студента е назначена стая")
    public void наСтудентаНазначенаСтая() {
        assertNotNull(ctx.application.getRoom());
    }

    @Then("стаята е със статус {string}")
    public void стаятаСъсСтатус(String status) {
        assertEquals(RoomStatus.valueOf(status), ctx.application.getRoom().getStatus());
    }

    @Then("одобряването е неуспешно")
    public void одобряванетоНеуспешно() {
        assertNotNull(ctx.error);
    }
}

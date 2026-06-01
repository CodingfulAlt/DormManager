package uni.pu.fmi;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uni.pu.fmi.exception.BusinessException;
import uni.pu.fmi.model.ApplicationStatus;
import uni.pu.fmi.model.RoomType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Стъпки за функционалност UC2 - Кандидатстване за стая.
 */
public class ApplicationSteps {

    private final TestContext ctx;

    public ApplicationSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("студентът вече има активна кандидатура")
    public void студентътВечеИмаКандидатура() {
        ctx.applicationService.apply(ctx.actor, RoomType.SINGLE);
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
            ctx.application = ctx.applicationService.apply(ctx.actor, type);
        } catch (BusinessException e) {
            ctx.error = e.getMessage();
        }
    }

    @When("студентът прегледа свободните стаи от тип {string}")
    public void студентътПрегледаСвободниСтаи(String type) {
        ctx.freeRooms = ctx.applicationService.viewFreeRooms(RoomType.valueOf(type));
    }

    @Then("кандидатурата е успешно подадена")
    public void кандидатуратаУспешна() {
        assertNotNull(ctx.application);
        assertNull(ctx.error);
    }

    @Then("статусът на кандидатурата е {string}")
    public void статусътНаКандидатурата(String status) {
        assertEquals(ApplicationStatus.valueOf(status), ctx.application.getStatus());
    }

    @Then("се показват {int} свободни стаи")
    public void сеПоказватСтаи(int count) {
        assertNotNull(ctx.freeRooms);
        assertEquals(count, ctx.freeRooms.size());
    }

    @Then("кандидатурата е неуспешна")
    public void кандидатуратаНеуспешна() {
        assertNotNull(ctx.error);
    }
}

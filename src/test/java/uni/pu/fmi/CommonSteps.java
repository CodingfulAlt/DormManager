package uni.pu.fmi;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import uni.pu.fmi.model.Role;
import uni.pu.fmi.model.Room;
import uni.pu.fmi.model.RoomStatus;
import uni.pu.fmi.model.RoomType;
import uni.pu.fmi.model.User;

import static org.junit.Assert.assertEquals;

/**
 * Общи стъпки, използвани от повече от една функционалност
 * (създаване на актьори, добавяне на стаи, проверка на грешки).
 */
public class CommonSteps {

    private final TestContext ctx;

    public CommonSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("студент с имейл {string}")
    public void студентСИмейл(String email) {
        ctx.actor = new User(0, "Студент", email, "hash", Role.STUDENT);
    }

    @Given("администратор с имейл {string}")
    public void администраторСИмейл(String email) {
        ctx.actor = new User(0, "Админ", email, "hash", Role.ADMIN);
    }

    @Given("в системата има свободна стая от тип {string}")
    public void имаСвободнаСтая(String type) {
        ctx.roomRepo.save(new Room(0, "101", RoomType.valueOf(type), 1, 150.0, RoomStatus.FREE));
    }

    @Given("в системата има {int} свободни стаи от тип {string}")
    public void имаНСвободниСтаи(int count, String type) {
        for (int i = 1; i <= count; i++) {
            ctx.roomRepo.save(new Room(0, "20" + i, RoomType.valueOf(type), 2, 200.0, RoomStatus.FREE));
        }
    }

    @Then("грешката е {string}")
    public void грешкатаЕ(String expected) {
        assertEquals(expected, ctx.error);
    }
}

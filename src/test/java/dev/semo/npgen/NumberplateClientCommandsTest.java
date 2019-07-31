package dev.semo.npgen;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import java.util.Map;
import dev.semo.npgen.service.PostClient;
import dev.semo.npgen.shell.NumberplateClientCommands;
import dev.semo.npgen.utils.NumberPlateUtility;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.shell.ConfigurableCommandRegistry;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.standard.StandardMethodTargetRegistrar;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostClient.class, NumberplateClientCommands.class})
public class NumberplateClientCommandsTest {

    private StandardMethodTargetRegistrar registrar = new StandardMethodTargetRegistrar();
    private ConfigurableCommandRegistry registry = new ConfigurableCommandRegistry();
    private Map<String, MethodTarget> commands;

    @Autowired
    public PostClient postClient;

    @Before
    public void setUp() {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(NumberplateClientCommands.class);
        registrar.setApplicationContext(context);
        registrar.register(registry);
    }

    @Test
    public void shouldAddTwoIntegers() {
        commands = registry.listCommands();

        MethodTarget methodTarget = commands.get("sum");

        assertThat(methodTarget, notNullValue());
        Assertions.assertThat(methodTarget.getGroup()).isEqualTo(
                "Numberplate Client Commands");
        assertThat(methodTarget.getHelp(), Is.is("Add up to sum."));
        assertThat(methodTarget.getMethod(), is(
                ReflectionUtils.findMethod(NumberplateClientCommands.class, "sum", int.class,
                        int.class)));
        assertThat(methodTarget.getAvailability().isAvailable(), is(true));
        assertEquals(3, ReflectionUtils.invokeMethod(methodTarget.getMethod(),
                methodTarget.getBean(), 1, 2));
    }

    @Test
    public void shouldSayHi() {
        commands = registry.listCommands();
        MethodTarget methodTarget = commands.get("say-hi");

        assertThat(methodTarget, notNullValue());
        Assertions.assertThat(methodTarget.getGroup()).isEqualTo(
                "Numberplate Client Commands");
        assertThat(methodTarget.getHelp(), Is.is("Saying hi to a given person's name."));
        assertThat(methodTarget.getMethod(), is(
                ReflectionUtils.findMethod(NumberplateClientCommands.class, "sayHi", String.class)));
        assertThat(methodTarget.getAvailability().isAvailable(), is(true));
        assertEquals("Hi, Nadine", ReflectionUtils.invokeMethod(methodTarget.getMethod(),
                methodTarget.getBean(), "Nadine"));
    }

    @Test
    public void shouldPostCamImage2kafka() throws FileNotFoundException {
        commands = registry.listCommands();
        MethodTarget methodTarget = commands.get("one");
        NumberPlateUtility np = new NumberPlateUtility();
        HttpStatus response = postClient.postNumberPlate(np.completeImage());
        assertThat(response, is(HttpStatus.OK));
    }
}

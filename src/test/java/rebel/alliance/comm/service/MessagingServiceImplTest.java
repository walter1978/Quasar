package rebel.alliance.comm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import rebel.alliance.comm.service.impl.MessagingServiceImpl;
import rebel.alliance.comm.service.model.Sat;
import rebel.alliance.comm.service.model.message.Message;

import java.util.Arrays;
import java.util.Optional;

public class MessagingServiceImplTest {
    private static String[] expectedMessage = {"este", "es", "un", "mensaje"};
    private MessagingService subject;

    @BeforeEach
    public void setup() {
        subject = new MessagingServiceImpl();
    }

    @Test
    public void getMessage_validData_messageIsCalculatedWithSuccess() {
        final Message kenobiMessage = new Message(Sat.KENOBI, new String[] {"", "este", "es", "un", "mensaje"});
        final Message skywalkerMessage = new Message(Sat.SKYWALKER, new String[] {"este", "", "un", "mensaje"});
        final Message satoMessage = new Message(Sat.SATO, new String[] {"", "", "es", "", "mensaje"});

        final Optional<String[]> messageOpt = subject.getMessage(Arrays.asList(kenobiMessage, skywalkerMessage, satoMessage));

        Assert.isTrue(Arrays.equals(expectedMessage, messageOpt.get()), "invalid message");
    }

    @Test
    public void getMessage_validData_messageIsCalculatedWithSuccess2() {
        final Message kenobiMessage = new Message(Sat.KENOBI, new String[] {"", "este", "es", "", ""});
        final Message skywalkerMessage = new Message(Sat.SKYWALKER, new String[] {"", "", "", "un", "mensaje"});
        final Message satoMessage = new Message(Sat.SATO, new String[] {"", "es", "un", ""});

        final Optional<String[]> messageOpt = subject.getMessage(Arrays.asList(kenobiMessage, skywalkerMessage, satoMessage));

        Assert.isTrue(Arrays.equals(expectedMessage, messageOpt.get()), "invalid message");
    }

    @Test
    public void getMessage_validData_messageIsCalculatedWithSuccess3() {
        final Message kenobiMessage = new Message(Sat.KENOBI, new String[] {"", "es", "un", ""});
        final Message skywalkerMessage = new Message(Sat.SKYWALKER, new String[] {"", "", "", "mensaje"});
        final Message satoMessage = new Message(Sat.SATO, new String[] {"este", "", "un", ""});

        final Optional<String[]> messageOpt = subject.getMessage(Arrays.asList(kenobiMessage, skywalkerMessage, satoMessage));

        Assert.isTrue(Arrays.equals(expectedMessage, messageOpt.get()), "invalid message");
    }

    @Test
    public void getMessage_dataFromTwoSatellitesIsMissing_messageCanNotBeCalculated() {
        final Message kenobiMessage = new Message(Sat.KENOBI, new String[] {"", "es", "un", ""});

        final Optional<String[]> messageOpt = subject.getMessage(Arrays.asList(kenobiMessage));

        Assert.isTrue(!messageOpt.isPresent(), "it shouldn't return a message, there is not enough data");
    }

    @Test
    public void getMessage_dataFromOneSatelliteIsMissing_messageCanNotBeCalculated() {
        final Message kenobiMessage = new Message(Sat.KENOBI, new String[] {"", "es", "un", ""});
        final Message skywalkerMessage = new Message(Sat.SKYWALKER, new String[] {"", "", "", "mensaje"});

        final Optional<String[]> messageOpt = subject.getMessage(Arrays.asList(kenobiMessage, skywalkerMessage));

        Assert.isTrue(!messageOpt.isPresent(), "it shouldn't return a message, there is not enough data");
    }

    @Test
    public void getMessage_oneMessageIsEmpty_messageCanNotBeCalculated() {
        final Message kenobiMessage = new Message(Sat.KENOBI, new String[] {"", "este", "es", "un", "mensaje"});
        final Message skywalkerMessage = new Message(Sat.SKYWALKER, new String[] {"este", "", "un", "mensaje"});
        final Message satoMessage = new Message(Sat.SATO, new String[] {});

        final Optional<String[]> messageOpt = subject.getMessage(Arrays.asList(kenobiMessage, skywalkerMessage, satoMessage));

        Assert.isTrue(!messageOpt.isPresent(), "it shouldn't return a message, there is not enough data");
    }
}

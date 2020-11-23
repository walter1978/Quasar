package rebel.alliance.comm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;
import rebel.alliance.comm.mapper.QuasarModelMapper;
import rebel.alliance.comm.service.impl.CommServiceImpl;
import rebel.alliance.comm.service.model.Sat;
import rebel.alliance.comm.service.model.comm.AllSatData;
import rebel.alliance.comm.service.model.comm.SatData;
import rebel.alliance.comm.service.model.comm.ShipData;
import rebel.alliance.comm.service.model.location.Distance;
import rebel.alliance.comm.service.model.location.ShipLocation;
import rebel.alliance.comm.service.model.message.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommServiceImplTest {
    @InjectMocks
    private CommService subject = new CommServiceImpl();
    @Mock
    private LocationService locationService;
    @Mock
    private MessagingService messagingService;
    @Mock
    private QuasarModelMapper modelMapper;
    private SatData kenobiSatData = new SatData(Sat.KENOBI, 100, new String[] {"the", "message"});
    private SatData skywalkerSatData = new SatData(Sat.SKYWALKER, 200, new String[] {"", "message"});
    private SatData satoSatData = new SatData(Sat.SATO, 300, new String[] {"the", ""});
    private AllSatData allSatData = new AllSatData(Arrays.asList(kenobiSatData, skywalkerSatData, satoSatData));
    private Distance distanceToKenobi = new Distance(Sat.KENOBI, 100);
    private Distance distanceToSkywalker = new Distance(Sat.SKYWALKER, 100);
    private Distance distanceToSato = new Distance(Sat.SATO, 100);
    private List<Distance> distanceList = Arrays.asList(distanceToKenobi, distanceToSato, distanceToSkywalker);
    private Message kenobiMessage = new Message(kenobiSatData.getSat(), kenobiSatData.getMessage());
    private Message skywalkerMessage = new Message(skywalkerSatData.getSat(), skywalkerSatData.getMessage());
    private Message satoMessage = new Message(satoSatData.getSat(), satoSatData.getMessage());
    private List<Message> messageList = Arrays.asList(kenobiMessage, skywalkerMessage, satoMessage);
    private ShipLocation shipLocation = new ShipLocation(300, 300);
    private String[] message = {"the", "message"};

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(modelMapper.mapToDistanceList(allSatData)).thenReturn(distanceList);
        when(modelMapper.mapToMessageList(allSatData)).thenReturn(messageList);
    }

    @Test
    public void calculateShipData_allSatDataWithValidValues_shipDataIsCalculatedWithSuccess() {
        when(locationService.getLocation(distanceList)).thenReturn(Optional.of(shipLocation));
        when(messagingService.getMessage(messageList)).thenReturn(Optional.of(message));

        final ShipData shipData = subject.calculateShipData(allSatData);

        Assert.isTrue(shipData.getMessage() == message, "invalid message");
        Assert.isTrue(shipData.getX() == shipLocation.getX(), "invalid x position");
        Assert.isTrue(shipData.getY() == shipLocation.getY(), "invalid y position");
    }

    @Test
    public void calculateShipData_allSatDataFailedToCalculateLocation_shipDataCanNotBeCalculated() {
        when(locationService.getLocation(distanceList)).thenReturn(Optional.empty());
        when(messagingService.getMessage(messageList)).thenReturn(Optional.of(message));

        final Exception exception = assertThrows(RuntimeException.class, () -> {
            subject.calculateShipData(allSatData);
        });

        Assert.isTrue(exception != null, "expected exception not thrown");
    }

    @Test
    public void calculateShipData_allSatDataFailedToCalculateMessage_shipDataCanNotBeCalculated() {
        when(locationService.getLocation(distanceList)).thenReturn(Optional.of(shipLocation));
        when(messagingService.getMessage(messageList)).thenReturn(Optional.empty());

        final Exception exception = assertThrows(RuntimeException.class, () -> {
            subject.calculateShipData(allSatData);
        });

        Assert.isTrue(exception != null, "expected exception not thrown");
    }

    @Test
    public void calculateShipData_satDataProvidedInStepsWithValidValues_shipDataIsCalculatedWithSuccess() {
        subject.updateSatData(kenobiSatData);
        subject.updateSatData(skywalkerSatData);
        subject.updateSatData(satoSatData);

        when(locationService.getLocation()).thenReturn(Optional.of(shipLocation));
        when(messagingService.getMessage()).thenReturn(Optional.of(message));

        final ShipData shipData = subject.calculateShipData();

        Assert.isTrue(shipData.getMessage() == message, "invalid message");
        Assert.isTrue(shipData.getX() == shipLocation.getX(), "invalid x position");
        Assert.isTrue(shipData.getY() == shipLocation.getY(), "invalid y position");

        verify(locationService, times(3)).updateDistance(any(Distance.class));
        verify(messagingService, times(3)).updateMessage(any(Message.class));
    }

    @Test
    public void calculateShipData_satDataProvidedInStepsFailedToCalculateLocation__shipDataCanNotBeCalculated() {
        subject.updateSatData(kenobiSatData);
        subject.updateSatData(skywalkerSatData);
        subject.updateSatData(satoSatData);

        when(locationService.getLocation(distanceList)).thenReturn(Optional.empty());
        when(messagingService.getMessage(messageList)).thenReturn(Optional.of(message));

        final Exception exception = assertThrows(RuntimeException.class, () -> {
            subject.calculateShipData();
        });

        Assert.isTrue(exception != null, "expected exception not thrown");

        verify(locationService, times(3)).updateDistance(any(Distance.class));
        verify(messagingService, times(3)).updateMessage(any(Message.class));
    }

    @Test
    public void calculateShipData_satDataProvidedInStepsFailedToCalculateMessage__shipDataCanNotBeCalculated() {
        subject.updateSatData(kenobiSatData);
        subject.updateSatData(skywalkerSatData);
        subject.updateSatData(satoSatData);

        when(locationService.getLocation(distanceList)).thenReturn(Optional.of(shipLocation));
        when(messagingService.getMessage(messageList)).thenReturn(Optional.empty());

        final Exception exception = assertThrows(RuntimeException.class, () -> {
            subject.calculateShipData();
        });

        Assert.isTrue(exception != null, "expected exception not thrown");

        verify(locationService, times(3)).updateDistance(any(Distance.class));
        verify(messagingService, times(3)).updateMessage(any(Message.class));
    }
}

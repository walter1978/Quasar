package rebel.alliance.comm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rebel.alliance.comm.Constants;
import rebel.alliance.comm.service.MessagingService;
import rebel.alliance.comm.service.model.Sat;
import rebel.alliance.comm.service.model.message.Message;

import java.util.*;

@Service
public class MessagingServiceImpl implements MessagingService {
    private static final Logger logger = LoggerFactory.getLogger(MessagingServiceImpl.class);
    private Map<Sat, String[]> messageMap = new HashMap<>();

    @Override
    public Optional<String[]> getMessage(List<Message> messageList) {
        final Optional<Message> kenobiMessage = getMessage(messageList, Sat.KENOBI);
        final Optional<Message> skywalkerMessage = getMessage(messageList, Sat.SKYWALKER);
        final Optional<Message> satoMessage = getMessage(messageList, Sat.SATO);

        if (isValidMessage(kenobiMessage) && isValidMessage(skywalkerMessage) && isValidMessage(satoMessage)) {
            return Optional.ofNullable(calculateMessage(kenobiMessage.get().getMessage(), skywalkerMessage.get().getMessage(), satoMessage.get().getMessage()));
        }
        return Optional.empty();
    }

    @Override
    public void updateMessage(Message message) {
        if (isValidMessage(Optional.ofNullable(message))) {
            messageMap.put(message.getSat(), message.getMessage());
        }
    }

    @Override
    public Optional<String[]> getMessage() {
        if (messageMap.size() == Constants.SATELLITE_COUNT) {
            final String[] message = calculateMessage(messageMap.get(Sat.KENOBI), messageMap.get(Sat.SKYWALKER), messageMap.get(Sat.SATO));
            if (message != null) {
                messageMap.clear();
                return Optional.of(message);
            }
        } else {
            logger.warn(String.format("There is not enough data to start the message calculation, messages count: %s" + messageMap.size()));
        }

        return Optional.empty();
    }

    private String[] calculateMessage(String[] ...messages) {
        try {
            swapLongedMessageToFirstPosition(messages);
            final String[] baseMessage = messages[0];   // uses the longest message as base message

            // replace words from other messages into the base message starting from right side
            for (int i = 1; i < Constants.SATELLITE_COUNT; i++) {
                final String[] message = messages[i];
                final int diffWithBaseMessage = baseMessage.length - message.length;
                for (int j = message.length - 1; j >= 0; j--) {
                    String word = message[j];
                    if (isNotEmptyWord(word)) {
                        baseMessage[j + diffWithBaseMessage] = word;
                    }
                }
            }

            // removes blank spaces from left side
            return Arrays.stream(baseMessage).filter(word -> word.trim().length() > 0).toArray(String[]::new);
        } catch (Exception e) {
            logger.error("Error calculating the message", e);
        }

        return null;
    }

    private void swapLongedMessageToFirstPosition(String[] ...messages) {
        int index = 0;
        int length = 0;

        for (int i = 0; i < messages.length; i++) {
            if (messages[i].length > length) {
                length = messages[i].length;
                index = i;
            }
        }

        final String[] firstMessage = messages[0];
        messages[0] = messages[index];
        messages[index] = firstMessage;
    }

    private boolean isNotEmptyWord(String word) {
        return (word != null && !(word.trim().length() == 0));
    }

    private Optional<Message> getMessage(List<Message> messageList, Sat sat) {
        return messageList.stream().filter(distance -> distance.getSat().equals(sat)).findFirst();
    }

    private boolean isValidMessage(Optional<Message> messageOpt) {
        final boolean valid = messageOpt.isPresent() && messageOpt.get().getMessage() != null && messageOpt.get().getMessage().length > 0;
        if (!valid) {
            logger.error(String.format("Invalid message: %s", messageOpt));
        }
        return valid;
    }
}

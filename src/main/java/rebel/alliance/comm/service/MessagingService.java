package rebel.alliance.comm.service;

import rebel.alliance.comm.service.model.message.Message;

import java.util.List;
import java.util.Optional;

/**
 * Service for messaging processing
 *
 */
public interface MessagingService {
    /**
     * Reconstruct a message based on messages received from 3 satellites
     *
     * @param messageList message list
     * @return an array containing the message
     */
    Optional<String[]> getMessage(List<Message> messageList);

    /**
     * Updates the message received from a specific satellite
     *
     * @param message message
     */
    void updateMessage(Message message);

    /**
     *  Reconstruct a message based on messages received from 3 satellites
     * Uses the latest data collected (updateMessage method)
     *
     * @return
     */
    Optional<String[]> getMessage();
}

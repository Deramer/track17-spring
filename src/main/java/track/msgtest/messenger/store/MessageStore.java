package track.msgtest.messenger.store;

import java.util.List;

import track.msgtest.messenger.messages.Message;


public interface MessageStore {
    /**
     * получаем список ид пользователей заданного чата
     */
    List<Long> getUsersIdFromChat(Long chatId);

    /**
     * Список сообщений из чата
     */
    List<Long> getMessagesFromChat(Long chatId);

    /**
     * Получить информацию о сообщении
     */
    Message getMessageById(Long messageId);

    /**
     * Добавить сообщение в чат
     */
    void addMessage(Long chatId, Message message);

    /**
     * Добавить пользователя к чату
     */
    void addUserToChat(Long userId, Long chatId);

    List<Long> getChatsByUserId(Long userId);

    Long getOrAddChatWithUsers(List<Long> usersId);
}

package ru.home.botfortuna.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.home.botfortuna.model.UserProfileData;

/**
 * Интерфейс MongoDB
 *
 * @author Igor Khristiuk
 */
@Repository
public interface UserProfileMongoRepository extends MongoRepository<UserProfileData, String> {
    UserProfileData findByChatId(String chatId);
    void deleteByChatId(String chatId);
}

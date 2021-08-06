package ru.home.botfortuna.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.home.botfortuna.model.UserProfileData;

import java.util.List;

/**
 * Интерфейс MongoDB
 *
 * @author Igor Khristiuk
 */
@Repository
public interface UserProfileMongoRepository extends MongoRepository<UserProfileData, String> {
    List<UserProfileData> findByChatId(String chatId);
    void deleteByChatId(String chatId);
}

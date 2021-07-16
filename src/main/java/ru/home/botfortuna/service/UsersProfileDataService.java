package ru.home.botfortuna.service;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.home.botfortuna.model.UserProfileData;
import ru.home.botfortuna.repository.UserProfileMongoRepository;

import java.util.List;

/**
 * @author Igor Khristiuk
 */
@Service
@Component
public class UsersProfileDataService{
    private UserProfileMongoRepository profileMongoRepository;

    public UsersProfileDataService(UserProfileMongoRepository profileMongoRepository) {
        this.profileMongoRepository = profileMongoRepository;
    }

    public List<UserProfileData> getAllProfiles() {
        return profileMongoRepository.findAll();
    }

    public void saveUserProfileData(UserProfileData userProfileData) {
        profileMongoRepository.save(userProfileData);
    }

    public void deleteUsersProfileData(String profileDataId) {
        profileMongoRepository.deleteById(profileDataId);
    }

    public UserProfileData getUserProfileData(String chatId) {
        return profileMongoRepository.findByChatId(chatId);
    }
}

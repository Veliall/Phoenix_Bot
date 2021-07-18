package ru.home.botfortuna.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Данные анкеты участника
 *
 * @author Igor Khristiuk
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "Students")
public class UserProfileData implements Serializable {
    @Id
    String id;
    String fullName;
    String birthday;
    String school;
    String address;
    String phone;

    String parentFullName;
    String parentBirthday;
    String job;
    String parentPhone;
    String chatId;

    @Override
    public String toString() {
        return String.format("ФИО ребёнка: %s%nДата рождения: %s%nШкола и класс: %s%nАдресс: %s%n" +
                        "Телефон: %s%nФИО родителя: %s%nДата рождения родителя: %s%n" +
                        "Место работы и должность: %s%nТелефон: %s",
                getFullName(), getBirthday(), getSchool(), getAddress(),
                getPhone(), getParentFullName(),getParentBirthday(), getJob(), getParentPhone());
    }
}
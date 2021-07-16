package ru.home.botfortuna.service;

import org.checkerframework.checker.units.qual.C;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Генерируем предсказание
 * @author Igor Khristiuk
 */
@Service
@Component
public class PredictionService {

        private final Random random = new Random();
        private ReplyMessageService messagesService;

        public PredictionService(ReplyMessageService messagesService) {
            this.messagesService = messagesService;
        }

        public String getPrediction() {
            int predictionNumber = random.nextInt(5);
            String replyMessagePropertie = String.format("%s%d", "reply.prediction", predictionNumber);
            return messagesService.getReplyText(replyMessagePropertie);
        }
    }


package M1.S2.TPS.notification.controller;

import M1.S2.TPS.notification.dto.MockUserRegisteredRequest;
import M1.S2.TPS.notification.messaging.MockEventPublisher;
import M1.S2.TPS.notification.model.UserRegisteredEvent;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification/mock")
public class NotificationMockController {

    private final MockEventPublisher mockEventPublisher;

    public NotificationMockController(MockEventPublisher mockEventPublisher) {
        this.mockEventPublisher = mockEventPublisher;
    }

    @PostMapping("/user-registered")
    public ResponseEntity<UserRegisteredEvent> publishMockUserRegistered(
            @Valid @RequestBody MockUserRegisteredRequest request
    ) {
        UserRegisteredEvent event = mockEventPublisher.publishMockUserRegistered(request.userId(), request.email());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(event);
    }
}

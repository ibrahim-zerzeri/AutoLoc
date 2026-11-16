package tn.esprit.tpautoloc.service.factory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.service.observer.EmailNotificationObserver;
import tn.esprit.tpautoloc.service.observer.IReservationObserver;
import tn.esprit.tpautoloc.service.observer.SmsNotificationObserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class NotificationFactoryTest {

    @Autowired
    private NotificationFactory notificationFactory;

    @Test
    void doitCreerObservateurEmail() {
        IReservationObserver observer = notificationFactory.creerObservateur("EMAIL");
        assertThat(observer).isInstanceOf(EmailNotificationObserver.class);
    }

    @Test
    void doitCreerObservateurSms() {
        IReservationObserver observer = notificationFactory.creerObservateur("SMS");
        assertThat(observer).isInstanceOf(SmsNotificationObserver.class);
    }

    @Test
    void doitEchouerPourCanalInconnu() {
        assertThrows(IllegalArgumentException.class, () -> notificationFactory.creerObservateur("WHATSAPP"));
    }
}

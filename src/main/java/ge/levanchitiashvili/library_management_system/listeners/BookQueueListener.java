package ge.levanchitiashvili.library_management_system.listeners;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BookQueueListener {

    @JmsListener(destination = "bookQueue")
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }
}

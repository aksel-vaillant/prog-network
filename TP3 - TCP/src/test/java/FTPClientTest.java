import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FTPClientTest {
    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() throws IOException {
        FTPClient client = new FTPClient();
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("hello server");

        System.out.println("Message from client : hello server");
        System.out.println("Checking message if they are equals... " + response);

        assertEquals("hello client", response);
    }
}
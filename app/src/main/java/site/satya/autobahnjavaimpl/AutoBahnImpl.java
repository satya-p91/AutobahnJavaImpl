package site.satya.autobahnjavaimpl;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.types.CloseDetails;
import io.crossbar.autobahn.wamp.types.EventDetails;
import io.crossbar.autobahn.wamp.types.Publication;
import io.crossbar.autobahn.wamp.types.SessionDetails;
import io.crossbar.autobahn.wamp.types.Subscription;

public class AutoBahnImpl {

    private static final Logger LOGGER = Logger.getLogger("PubSub");
    private Client client;

    public void connect(String url){

        //Creating session
        Session session = new Session();

        //session callbacks
        session.addOnConnectListener(this::onConnectCallback);
        session.addOnJoinListener(this::onJoinCallback);
        session.addOnJoinListener(this::demonstratePublish);
        session.addOnLeaveListener(this::onLeaveCallback);
        session.addOnDisconnectListener(this::onDisconnectCallback);

        //Pubsub Client with session, url and realm
        client = new Client(session, url, "realm1");
        client.connect();
    }

    private void onJoinCallback(Session session, SessionDetails sessionDetails) {
        LOGGER.info("Session joined, ID=" + session.getID());

        //callback Interface object for handling events on three topics
        ICounterSubscriber subscriber = new ICounterSubscriber() {

            @Override
            public void agentStatusEvent(List<Object> args, Map<String, Object> kwargs, EventDetails details) {
                LOGGER.info(String.format("oncounter event, counter value=%s", kwargs.toString()));
            }

            @Override
            public void agentUpdateEvent(List<Object> args, Map<String, Object> kwargs, EventDetails details) {
                LOGGER.info(String.format("oncounter event, counter value=%s", kwargs.toString()));
            }

            @Override
            public void webrtcEvent(List<Object> args, Map<String, Object> kwargs, EventDetails details) {
                LOGGER.info(String.format("oncounter event, counter value=%s", kwargs.toString()));
            }
        };

        List<CompletableFuture<Subscription>> subFuture = session.getReflectionServices().registerSubscriber(subscriber);

        subFuture.forEach(subscriptionCompletableFuture -> {
            subscriptionCompletableFuture.whenComplete((subscription, throwable) -> {
                if(throwable == null){
                    LOGGER.info(String.format("Subscribed to topic: %s", subscription.topic));
                }
            });
        });

        //subFuture.get(0).thenAccept(subscription -> LOGGER.info(String.format("Subscribed to topic: %s", subscription.topic)));
    }

    public void demonstratePublish(Session session, SessionDetails details) {
        // Publish to a topic that takes a single arguments
        List<Object> args = Arrays.asList("testing pusub publish", 900, "dummy data","is it working?");
        CompletableFuture<Publication> pubFuture = session.publish("io.supportgenie.pubsub.status", args);
        pubFuture.thenAccept(publication -> System.out.println("Published successfully"));
        // Shows we can separate out exception handling
        pubFuture.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private void onConnectCallback(Session session) {
        LOGGER.info("Session connected, ID=" + session.getID());
    }

    private void onDisconnectCallback(Session session, boolean b) {
        LOGGER.info(String.format("Session with ID=%s, disconnected.", session.getID()));
    }

    private void onLeaveCallback(Session session, CloseDetails closeDetails) {
        LOGGER.info(String.format("Left reason=%s, message=%s", closeDetails.reason, closeDetails.message));
    }

}

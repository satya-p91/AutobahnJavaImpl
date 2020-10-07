package site.satya.autobahnjavaimpl;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.types.CloseDetails;
import io.crossbar.autobahn.wamp.types.Publication;
import io.crossbar.autobahn.wamp.types.SessionDetails;
import io.crossbar.autobahn.wamp.types.Subscription;

public class AutoBahnImpl {

    private static final Logger LOGGER = Logger.getLogger("PubSub");

    public void connect(String url){

        //Setting session
        Session session = new Session();

        //session callbacks
        session.addOnConnectListener(this::onConnectCallback);
        session.addOnJoinListener(this::onJoinCallback);
        session.addOnJoinListener(this::demonstratePublish);
        session.addOnLeaveListener(this::onLeaveCallback);
        session.addOnDisconnectListener(this::onDisconnectCallback);

        //Pubsub Client with session, url and realm
        Client client = new Client(session, url, "realm1");
        client.connect().whenComplete((exitInfo, throwable) -> {
            if(throwable!= null){
                throwable.printStackTrace();
            }else {
                LOGGER.info("exitCode: "+exitInfo.code);
            }
        });
    }

    private void onJoinCallback(Session session, SessionDetails sessionDetails) {
        LOGGER.info("Session joined, ID=" + session.getID());

        //callback Interface object for handling events on three topics
        ICounterSubscriber subscriber = new ICounterSubscriber() {

            @Override
            public void agentStatusEvent(int counter) {
                LOGGER.info(String.format("oncounter event, counter value=%s", counter));

            }

            @Override
            public void agentUpdateEvent(int counter) {
                LOGGER.info(String.format("oncounter event, counter value=%s", counter));

            }

            @Override
            public void webrtcEvent(int counter) {
                LOGGER.info(String.format("oncounter event, counter value=%s", counter));

            }
        };

        List<CompletableFuture<Subscription>> subFuture = session.getReflectionServices().registerSubscriber(subscriber);

        subFuture.forEach(subscriptionCompletableFuture -> {
            subscriptionCompletableFuture.thenAccept(subscription ->
                    LOGGER.info(String.format("Subscribed to topic: %s", subscription.topic)));
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

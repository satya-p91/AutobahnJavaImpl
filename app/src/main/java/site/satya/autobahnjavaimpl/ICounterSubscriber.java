package site.satya.autobahnjavaimpl;

import java.util.List;
import java.util.Map;

import io.crossbar.autobahn.wamp.reflectionRoles.WampTopic;
import io.crossbar.autobahn.wamp.types.EventDetails;

public interface ICounterSubscriber {
    @WampTopic("io.supportgenie.compamy.agent.status.5c01af56830f7879b727607d")
    void agentStatusEvent(List<Object> args, Map<String, Object> kwargs, EventDetails details);

    @WampTopic("io.supportgenie.agent.updated.5c01af56830f7879b727607d")
    void agentUpdateEvent(List<Object> args, Map<String, Object> kwargs, EventDetails details);

    @WampTopic("io.supportgenie.webrtc.5c01af56830f7879b727607d")
    void webrtcEvent(List<Object> args, Map<String, Object> kwargs, EventDetails details);


}

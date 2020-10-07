///////////////////////////////////////////////////////////////////////////////
//
//   AutobahnJava - http://crossbar.io/autobahn
//
//   Copyright (c) Crossbar.io Technologies GmbH and contributors
//
//   Licensed under the MIT License.
//   http://www.opensource.org/licenses/mit-license.php
//
///////////////////////////////////////////////////////////////////////////////

package site.satya.autobahnjavaimpl;

import io.crossbar.autobahn.wamp.reflectionRoles.WampTopic;

public interface ICounterSubscriber {
    @WampTopic("io.supportgenie.compamy.agent.status.5c01af56830f7879b727607d")
    void agentStatusEvent(int counter);

    @WampTopic("iio.supportgenie.agent.updated.5c01af56830f7879b727607d")
    void agentUpdateEvent(int counter);

    @WampTopic("io.supportgenie.webrtc.5c01af56830f7879b727607d")
    void webrtcEvent(int counter);


}

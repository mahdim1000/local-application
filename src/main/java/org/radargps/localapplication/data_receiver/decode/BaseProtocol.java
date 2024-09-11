/*
 * Copyright 2015 - 2022 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.radargps.localapplication.data_receiver.decode;

import java.util.*;

public abstract class BaseProtocol implements Protocol {

    private final String name;
    private final List<TrackerConnector> connectorList = new LinkedList<>();

    public static String nameFromClass(Class<?> clazz) {
        String className = clazz.getSimpleName();
        return className.substring(0, className.length() - 8).toLowerCase();
    }

    public BaseProtocol() {
        name = nameFromClass(getClass());
    }

    @Override
    public String getName() {
        return name;
    }

    protected void addServer(TrackerServer server) {
        connectorList.add(server);
    }

    protected void addClient(TrackerClient client) {
        connectorList.add(client);
    }

    @Override
    public Collection<TrackerConnector> getConnectorList() {
        return connectorList;
    }

}

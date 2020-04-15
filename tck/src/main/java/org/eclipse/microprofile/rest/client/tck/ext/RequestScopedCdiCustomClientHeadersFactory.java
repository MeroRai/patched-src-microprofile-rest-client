/*
 * Copyright 2020 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.microprofile.rest.client.tck.ext;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

@RequestScoped
public class RequestScopedCdiCustomClientHeadersFactory implements ClientHeadersFactory {

    public static MultivaluedMap<String, String> passedInOutgoingHeaders = new MultivaluedHashMap<>();
    public static boolean isIncomingHeadersMapNull;
    public static boolean isOutgoingHeadersMapNull;
    public static boolean invoked;

    @Inject
    private RequestScopedCounter counter;

    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
        MultivaluedMap<String, String> clientOutgoingHeaders) {
        invoked = true;
        isIncomingHeadersMapNull = incomingHeaders == null;
        isOutgoingHeadersMapNull = clientOutgoingHeaders == null;
        if (!isOutgoingHeadersMapNull) {
            passedInOutgoingHeaders.putAll(clientOutgoingHeaders);
        }

        MultivaluedMap<String, String> returnVal = new MultivaluedHashMap<>();
        returnVal.putSingle("FactoryHeader", "factoryValue");
        clientOutgoingHeaders.forEach((k, v) -> {
            returnVal.putSingle(k, v.get(0) + "Modified"); });

        if (counter != null) {
            returnVal.putSingle("CDI_INJECT_COUNT", "" + counter.count());
        }
        return returnVal;
    }
}

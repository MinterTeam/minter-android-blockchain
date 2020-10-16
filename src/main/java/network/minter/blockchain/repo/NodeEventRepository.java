/*
 * Copyright (C) by MinterTeam. 2020
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.blockchain.repo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.api.NodeEventEndpoint;
import network.minter.blockchain.models.EventList;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.core.internal.log.Mint;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class NodeEventRepository extends DataRepository<NodeEventEndpoint> implements DataRepository.Configurator {
    public NodeEventRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Observable<EventList> getEvents(long blockNumber) {
        return getInstantService().getByHeight(String.valueOf(blockNumber));
    }

    public Observable<EventList> getEvents(BigInteger blockNumber) {
        return getInstantService().getByHeight(blockNumber.toString());
    }

    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(EventList.class, new EventTypeDeserializer(MinterBlockChainSDK.getInstance().getGsonBuilder()));
    }

    @Nonnull
    @Override
    protected Class<NodeEventEndpoint> getServiceClass() {
        return NodeEventEndpoint.class;
    }

    public static final class EventTypeDeserializer implements JsonDeserializer<EventList> {
        private final Gson mGson;

        EventTypeDeserializer(GsonBuilder builder) {
            mGson = builder.create();
        }

        @Override
        public EventList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            EventList eventList = new EventList();
            eventList.events = new ArrayList<>(0);
            if (json.isJsonNull() || json.getAsJsonObject().isJsonNull() || !json.getAsJsonObject().has("events")) {
                return eventList;
            }

            JsonArray arr = json.getAsJsonObject().get("events").getAsJsonArray();

            for (int i = 0; i < arr.size(); i++) {
                EventList.EventItem event = new EventList.EventItem();
                JsonObject item = arr.get(i).getAsJsonObject();

                event.type = EventList.Type.findByName(item.get("type").getAsString());
                if (event.type == null) {
                    Mint.e("Unknown event type: %s", item.get("type").getAsString());
                    continue;
                }
                JsonObject itemValue = item.get("value").getAsJsonObject();
                event.value = mGson.fromJson(itemValue, event.type.getCls());

                eventList.events.add(event);
            }

            return eventList;
        }
    }


}

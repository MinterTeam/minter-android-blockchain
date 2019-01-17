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
import java.util.ArrayList;

import javax.annotation.Nonnull;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.api.BlockChainEventEndpoint;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.EventList;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.core.internal.log.Mint;
import retrofit2.Call;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BlockChainEventRepository extends DataRepository<BlockChainEventEndpoint> implements DataRepository.Configurator {
    public BlockChainEventRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(EventList.class, new EventTypeDeserializer(MinterBlockChainApi.getInstance().getGsonBuilder()));
    }

    public Call<BCResult<EventList>> getEvents(long blockHeight) {
        return getInstantService().getByHeight(blockHeight);
    }

    @Nonnull
    @Override
    protected Class<BlockChainEventEndpoint> getServiceClass() {
        return BlockChainEventEndpoint.class;
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

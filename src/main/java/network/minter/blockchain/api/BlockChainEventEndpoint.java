package network.minter.blockchain.api;

import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.EventList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public interface BlockChainEventEndpoint {

    @GET("/events")
    Call<BCResult<EventList>> getByHeight(@Query("height") long height);
}

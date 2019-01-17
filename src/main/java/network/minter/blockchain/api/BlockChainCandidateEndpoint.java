package network.minter.blockchain.api;

import java.util.List;

import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.CandidateItem;
import network.minter.blockchain.models.CandidateStatus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public interface BlockChainCandidateEndpoint {

    @GET("/candidate")
    Call<BCResult<CandidateItem>> getCandidate(@Query("pubkey") String pubKey);

    @GET("/candidate")
    Call<BCResult<CandidateItem>> getCandidate(@Query("pubkey") String pubKey, long blockHeight);

    @GET("/candidates")
    Call<BCResult<List<CandidateStatus>>> getCandidates(@Query("height") long blockHeight);
}

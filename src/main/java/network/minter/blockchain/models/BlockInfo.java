package network.minter.blockchain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigInteger;
import java.util.List;

import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class BlockInfo {

    @SerializedName("hash")
    @Expose
    public BytesData hash;
    @SerializedName("height")
    @Expose
    public long height;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("num_txs")
    @Expose
    public BigInteger numTxs;
    @SerializedName("total_txs")
    @Expose
    public BigInteger totalTxs;
    @SerializedName("transactions")
    @Expose
    public List<HistoryTransaction> transactions = null;
    @SerializedName("block_reward")
    @Expose
    public BigInteger blockReward;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("proposer")
    @Expose
    public MinterPublicKey proposer;
    @SerializedName("validators")
    @Expose
    public List<ValidatorInfo> validators = null;
}

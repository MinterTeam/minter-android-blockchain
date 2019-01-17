package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class CandidateItem {

    @SerializedName("reward_address")
    public MinterAddress rewardAddress;
    @SerializedName("owner_address")
    public MinterAddress ownerAddress;
    @SerializedName("total_stake")
    public BigInteger totalStake;
    @SerializedName("pubkey")
    public MinterPublicKey pubKey;
    @SerializedName("commission")
    public int commission;
    @SerializedName("stakes")
    public List<StakeInfo> stakes = Collections.emptyList();
    @SerializedName("created_at_block")
    public long createdAtBlock;
    @SerializedName("status")
    public int status;

    @Parcel
    public static class StakeInfo {
        @SerializedName("owner")
        public MinterAddress owner;
        @SerializedName("coin")
        public String coin;
        @SerializedName("value")
        public BigInteger value;
        @SerializedName("bip_value")
        public BigInteger bipValue;
    }
}

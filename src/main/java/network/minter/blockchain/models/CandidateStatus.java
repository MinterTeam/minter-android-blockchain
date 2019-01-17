package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigInteger;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class CandidateStatus {
    @SerializedName("reward_address")
    public MinterAddress rewardAddress;
    @SerializedName("owner_address")
    public MinterAddress ownerAddres;
    @SerializedName("total_stake")
    public BigInteger totalStake;
    @SerializedName("pubkey")
    public MinterPublicKey pubKey;
    @SerializedName("commission")
    public int commission;
    @SerializedName("created_at_block")
    public long createdAtBlock;
    @SerializedName("status")
    public int status;
}

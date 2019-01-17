package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class ValidatorInfo {
    @SerializedName("pubkey")
    public MinterPublicKey publicKey;
    public boolean signed;
}

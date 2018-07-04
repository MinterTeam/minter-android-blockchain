package network.minter.blockchainapi.models;

import com.google.gson.annotations.SerializedName;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionSendResult extends BCResult<Void> {
    @SerializedName("tx_hash")
    public String txHash;
}

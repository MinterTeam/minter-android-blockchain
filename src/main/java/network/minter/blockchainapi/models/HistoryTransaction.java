package network.minter.blockchainapi.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

import network.minter.blockchainapi.models.operational.Operation;
import network.minter.blockchainapi.models.operational.OperationType;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class HistoryTransaction {
    public BytesData hash;
    public int height;
    public int index;
    public OperationType type;
    public MinterAddress from;
    public BigInteger nonce;
    public BigInteger gasPrice;
    @SerializedName("tx_result")
    public TxResult txResult;
    public Object data;
    public String payload; //@TODO what is it?

    public <T extends Operation> T getData() {
        return (T) data;
    }

    public static class TxResult {
        public BigInteger gasWanted;
        public BigInteger gasUsed;
        public List<Tag> tags;
        public Object fee; //@TODO
    }

    public static class Tag {
        public String key;
        public String value;
    }
}

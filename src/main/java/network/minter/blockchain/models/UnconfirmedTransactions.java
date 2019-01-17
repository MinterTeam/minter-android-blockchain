package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import network.minter.blockchain.models.operational.TransactionSign;

import static network.minter.core.internal.common.Preconditions.firstNonNull;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class UnconfirmedTransactions {
    @SerializedName("n_txs")
    public int count;
    @SerializedName("txs")
    public List<TransactionSign> signatures;

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public Iterator<TransactionSign> iterator() {
        return getSigs().iterator();
    }

    @SuppressWarnings("unchecked")
    private List<TransactionSign> getSigs() {
        return firstNonNull(signatures, Collections.emptyList());
    }
}

/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
 * @link https://github.com/edwardstock
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.blockchain.models.operational;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import network.minter.core.util.RLP;

import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.common.Preconditions.firstNonNull;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class Operation implements Parcelable {
    private final Transaction mTx;

    public Operation(@NonNull Transaction rawTx) {
        mTx = checkNotNull(rawTx, "Transaction must be set");
    }

    protected Operation(Parcel in) {
        mTx = (Transaction) in.readValue(Transaction.class.getClassLoader());
    }

    public Transaction build() throws OperationInvalidDataException {
        final Transaction tx = mTx.setData(this);
        FieldsValidationResult validated = validate();
        if (validated == null) {
            validated = tx.validate();
        } else {
            validated.addResult(tx.validate());
        }

        // check tx common model and operation has valid data
        if (validated != null && !validated.isValid()) {
            // if not, trigger error
            final String title = firstNonNull(validated.getTitle(), String.format("Invalid %s operation data", getType().name()));
            final String body = validated.getInvalidFieldsMessages();

            throw new OperationInvalidDataException(
                    String.format("%s\nInvalid fields:\n%s", title, body),
                    validated.getFieldMessageMap()
            );
        }

        // everything is fine, return tx
        return tx;
    }

    public abstract OperationType getType();

    @Override
    public int describeContents() {
        return 0;
    }

    @CallSuper
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mTx);
    }

    protected abstract void decodeRLP(@NonNull byte[] rlpEncodedData);

    protected byte[] fromRawRlp(int idx, Object[] raw) {
        return (byte[]) raw[idx];
    }

    @Nullable
    protected abstract FieldsValidationResult validate();

    protected Transaction getTx() {
        return mTx;
    }

    /**
     * Encodes all create fields via RLP
     *
     * @return encoded byte[]
     * @see RLP
     */
    @NonNull
    protected abstract byte[] encodeRLP();


}

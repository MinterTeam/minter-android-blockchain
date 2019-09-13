/*
 * Copyright (C) by MinterTeam. 2019
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
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

import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static network.minter.core.internal.common.Preconditions.firstNonNull;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class Operation extends RLPSerializable implements Parcelable {
    private final Transaction mTx;

    public Operation() {
        mTx = null;
    }

    public Operation(@Nonnull Transaction rawTx) {
        mTx = rawTx;
    }

    protected Operation(Parcel in) {
        mTx = (Transaction) in.readValue(Transaction.class.getClassLoader());
    }

    public Transaction build() throws OperationInvalidDataException {
        if (mTx == null) {
            throw new OperationInvalidDataException("Can't build operation, because transaction isn't passed", Collections.emptyList());
        }
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


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mTx);
    }

    @Nullable
    protected abstract FieldsValidationResult validate();

    protected Transaction getTx() {
        return mTx;
    }


}

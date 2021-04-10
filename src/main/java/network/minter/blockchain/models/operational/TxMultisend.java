/*
 * Copyright (C) by MinterTeam. 2020
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

/**
 * Transaction for sending coins to multiple addresses.
 * <p>
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxMultisend extends Operation {
    private List<TxSendCoin> mItems = new ArrayList<>();

    public TxMultisend() {
    }

    public TxMultisend(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    public List<TxSendCoin> getItems() {
        return mItems;
    }

    public TxSendCoin getItem(int index) {
        return mItems.size() > index && index > -1 ? mItems.get(index) : null;
    }

    public TxMultisend addItem(BigInteger coinId, MinterAddress recipient, BigDecimal value) {
        mItems.add(new TxSendCoin(getTx())
                .setCoinId(coinId)
                .setTo(recipient)
                .setValue(value));

        return this;
    }

    public TxMultisend addItem(long coinId, MinterAddress recipient, BigDecimal value) {
        mItems.add(new TxSendCoin(getTx())
                .setCoinId(coinId)
                .setTo(recipient)
                .setValue(value));

        return this;
    }

    public TxMultisend addItem(BigInteger coinId, MinterAddress recipient, CharSequence value) {
        mItems.add(new TxSendCoin(getTx())
                .setCoinId(coinId)
                .setTo(recipient)
                .setValue(value));

        return this;
    }

    public TxMultisend addItem(long coinId, MinterAddress recipient, CharSequence value) {
        mItems.add(new TxSendCoin(getTx())
                .setCoinId(coinId)
                .setTo(recipient)
                .setValue(value));

        return this;
    }

    /**
     * @param coin coin name to send
     * @param recipient String address
     * @param value Floating point string value. Precision up to 18 digits: 0.10203040506078090
     * @return
     */
    public TxMultisend addItem(BigInteger coinId, String recipient, @Nonnull final CharSequence decimalValue) {
        mItems.add(new TxSendCoin(getTx())
                .setCoinId(coinId)
                .setTo(recipient)
                .setValue(decimalValue));

        return this;
    }

    /**
     * @param coin coin name to send
     * @param recipient String address
     * @param value Floating point string value. Precision up to 18 digits: 0.10203040506078090
     * @return
     */
    public TxMultisend addItem(long coinId, String recipient, @Nonnull final CharSequence decimalValue) {
        mItems.add(new TxSendCoin(getTx())
                .setCoinId(coinId)
                .setTo(recipient)
                .setValue(decimalValue));

        return this;
    }

    public TxMultisend addItem(TxSendCoin txSendCoin) {
        mItems.add(txSendCoin);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.Multisend;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mItems", mItems.size() > 0, "You should add at least one send transaction data");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        Object[] items = (Object[]) decoded[0];
        mItems = new LinkedList<>();
        for (int i = 0; i < items.length; i++) {
            final TxSendCoin data = new TxSendCoin(getTx());
            // object array of object array contains 3 byte array (Coin/To/Value)
            data.decodeRaw(objArrToByteArrArr((Object[]) items[i]));
            mItems.add(i, data);
        }
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        final Object[][] items = new Object[mItems.size()][3];
        for (int i = 0; i < mItems.size(); i++) {
            items[i] = new Object[]{mItems.get(i).getCoinId(), mItems.get(i).getTo(), mItems.get(i).getValueBigInteger()};
        }

        return RLPBoxed.encode(new Object[]{items});

    }
}

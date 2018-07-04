/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
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

package network.minter.blockchainapi.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

import network.minter.blockchainapi.models.operational.Operation;
import network.minter.blockchainapi.models.operational.OperationType;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * minter-android-blockchain. 2018
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
	public String payload;

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

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

package network.minter.blockchain.transactions;

import network.minter.blockchain.BuildConfig;
import network.minter.blockchain.utils.DeepLinkBuilder;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public abstract class BaseTxTest {
    protected static final PrivateKey QA_KEY;
    protected static final MinterAddress QA_ADDRESS;
    protected static final PrivateKey TESTNET_KEY;
    protected static final MinterAddress TESTNET_ADDRESS;
    protected static final PrivateKey UNIT_KEY;
    protected static final MinterAddress UNIT_ADDRESS;

    static {
        try {
            MinterSDK.initialize();
            DeepLinkBuilder.BIP_WALLET_URL = DeepLinkBuilder.BIP_WALLET_TESTNET;
        } catch (NativeLoadException e) {
            throw new RuntimeException(e);
        }
        QA_KEY = PrivateKey.fromMnemonic(BuildConfig.QA_MNEMONIC);
        QA_ADDRESS = QA_KEY.getPublicKey().toMinter();
        TESTNET_KEY = PrivateKey.fromMnemonic(BuildConfig.TESTNET_MNEMONIC);
        TESTNET_ADDRESS = TESTNET_KEY.getPublicKey().toMinter();
        UNIT_KEY = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        UNIT_ADDRESS = UNIT_KEY.getPublicKey().toMinter();
    }
}

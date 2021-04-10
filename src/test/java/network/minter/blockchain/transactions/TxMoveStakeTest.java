/*
 * Copyright (C) by MinterTeam. 2021
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

import org.junit.Test;

import network.minter.blockchain.models.operational.OperationInvalidDataException;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxMoveStakeTest extends BaseTxTest {

    @Test
    public void testEncode()
            throws OperationInvalidDataException {
//        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
//        String expectedTx = "f8a1140201801bb850f84ea0325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350999999999a0325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350a380bc7d4808a010f0cf064dd59200000808001b845f8431ba096e59a5ba7cdccce28d6861a6a266c6b1e2e8bb87b86d1e97359f92c0349524da001cabc9a567adac325caa5ce64a32be2f789582cd71c1d4316dd9cfc3eea9d27";
//
//        Transaction tx = new Transaction.Builder(new BigInteger("20"))
//                .setBlockchainId(BlockchainID.TestNet)
//                .setGasCoinId(new BigInteger("0"))
//                .setGasPrice(new BigInteger("1"))
//                .moveStake()
//                .setFrom("Mp325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350999999999")
//                .setTo("Mp325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350a380bc7d4")
//                .setCoinId(DEFAULT_COIN_ID)
//                .setStake(new BigDecimal("5000"))
//                .build();
//
//        TransactionSign sign = tx.signSingle(privateKey);
//        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecode() {
//        final String expectedTx = "f8a1140201801bb850f84ea0325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350999999999a0325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350a380bc7d4808a010f0cf064dd59200000808001b845f8431ba096e59a5ba7cdccce28d6861a6a266c6b1e2e8bb87b86d1e97359f92c0349524da001cabc9a567adac325caa5ce64a32be2f789582cd71c1d4316dd9cfc3eea9d27";
//
//        Transaction tx = Transaction.fromEncoded(expectedTx);
//        assertEquals(new BigInteger("20"), tx.getNonce());
//        assertEquals(new BigInteger("1"), tx.getGasPrice());
//        assertEquals(new BigInteger("0"), tx.getGasCoinId());
//
//        TxMoveStake data = tx.getData();
//        assertEquals(new MinterPublicKey("Mp325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350999999999"), data.getFrom());
//        assertEquals(new MinterPublicKey("Mp325580a8baed04899252ae5b5f6167ee34ec0144f3401d88586b350a380bc7d4"), data.getTo());
//        assertEquals(new BigDecimal("5000"), data.getStake());
//        assertEquals(new BigInteger("0"), data.getCoinId());
    }
}

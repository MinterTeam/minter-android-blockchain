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

package network.minter.blockchain.repo;

import com.annimon.stream.Stream;

import java.util.List;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.blockchain.api.NodeAddressEndpoint;
import network.minter.blockchain.models.AddressInfo;
import network.minter.blockchain.models.AddressInfoList;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class NodeAddressRepository extends DataRepository<NodeAddressEndpoint> {
    public NodeAddressRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Observable<AddressInfo> getAddressInfo(String address) {
//        return getAddressInfo(new MinterAddress(address));
        return getInstantService().getAddressInfo(address);
    }

    public Observable<AddressInfo> getAddressInfo(@Nonnull MinterAddress address) {
        checkNotNull(address, "Public key required!");
        return getInstantService().getAddressInfo(address.toString());
    }


    public Observable<AddressInfo> getAddressInfo(@Nonnull MinterAddress address, long blockHeight, boolean includeDelegatedStakes) {
        checkNotNull(address, "Public key required!");
        return getInstantService().getAddressInfo(
                address.toString(),
                String.valueOf(blockHeight),
                String.valueOf(includeDelegatedStakes)
        );
    }

    public Observable<AddressInfoList> getAddressesInfo(List<MinterAddress> addressList, long blockHeight, boolean includeDelegatedStakes) {
        checkNotNull(addressList, "Address list can't be null");
        checkArgument(!addressList.isEmpty(), "Address list should contain at least 1 address");
        return getInstantService().getAddressesInfo(
                Stream.of(addressList).map(MinterAddress::toString).toList(),
                String.valueOf(blockHeight),
                String.valueOf(includeDelegatedStakes)
        );
    }

    public Observable<AddressInfoList> getAddressesInfo(List<MinterAddress> addressList) {
        checkNotNull(addressList, "Address list can't be null");
        checkArgument(!addressList.isEmpty(), "Address list should contain at least 1 address");
        return getInstantService().getAddressesInfo(
                Stream.of(addressList).map(MinterAddress::toString).toList()
        );
    }

    @Nonnull
    @Override
    protected Class<NodeAddressEndpoint> getServiceClass() {
        return NodeAddressEndpoint.class;
    }

}

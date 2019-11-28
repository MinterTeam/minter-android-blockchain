#!/usr/bin/env bash

set -e

chmod +x gradlew
chmod +x scripts/publish.sh

./scripts/publish.sh -f netTestRelease -s testnet -t bintray
./scripts/publish.sh -f netMainRelease -t bintray
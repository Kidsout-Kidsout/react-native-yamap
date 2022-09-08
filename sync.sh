#!/bin/bash

loc="$1"

if [ -z "$loc" ]; then
  echo "Provide directory"
  exit 1
fi

rsync -arvuz --delete ./src/ "$loc/src/"
rsync -arvuz --delete ./android/src/ "$loc/android/src/"
rsync -arvuz --delete ./ios/ "$loc/ios/"
rsync -arvuz --delete ./build/ "$loc/build/"
rsync -arvuz --delete ./sync.sh "$loc/sync.sh"
rsync -arvuz --delete ./package.json "$loc/package.json"
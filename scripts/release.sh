#!/usr/bin/env bash
#
# Release script.
#
# Bumps the version in package.json, creates a release commit and a
# matching `vX.Y.Z` tag (which the publish workflow listens on).
#
# Usage:
#   ./scripts/release.sh <semver-identifier>
#
# Where <semver-identifier> is anything `npm version` accepts, e.g.:
#   patch | minor | major | prepatch | preminor | premajor | prerelease
#   or an explicit version like 5.1.0
#
set -euo pipefail

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <semver-identifier>" >&2
  echo "  e.g. $0 patch | minor | major | 5.1.0" >&2
  exit 1
fi

IDENTIFIER="$1"

# Must be run from a clean working tree, otherwise the release commit
# would sweep up unrelated changes.
if [ -n "$(git status --porcelain)" ]; then
  echo "Error: working tree is not clean. Commit or stash your changes first." >&2
  exit 1
fi

# `npm version` updates package.json, commits it and creates a `v<version>` tag.
NEW_VERSION="$(npm version "$IDENTIFIER" -m "v%s")"

echo "Created release commit and tag: ${NEW_VERSION}"
echo "Push it with:"
echo "  git push && git push origin ${NEW_VERSION}"

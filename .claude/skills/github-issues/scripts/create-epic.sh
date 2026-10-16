#!/usr/bin/env bash
set -euo pipefail

REPO=4sq4r/serendipity

if [ $# -ne 2 ]; then
  echo "usage: create-epic.sh <title> <body-file>" >&2
  exit 1
fi

title="$1"
body_file="$2"

url=$(gh issue create -R "$REPO" --title "$title" --label epic --body-file "$body_file")
echo "${url##*/}"

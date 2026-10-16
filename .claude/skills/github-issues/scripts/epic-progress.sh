#!/usr/bin/env bash
set -euo pipefail

REPO=4sq4r/serendipity

if [ $# -ne 1 ]; then
  echo "usage: epic-progress.sh <epic-number>" >&2
  exit 1
fi

epic="$1"

gh issue view "$epic" -R "$REPO" --json number,title,state --jq '"#\(.number) \(.title) [\(.state)]"'
gh api "repos/$REPO/issues/$epic/sub_issues" --paginate \
  --jq '.[] | "  #\(.number) \(.title) [\(.state)]"'
gh api "repos/$REPO/issues/$epic" \
  --jq '.sub_issues_summary | "closed \(.completed) of \(.total)"'

#!/usr/bin/env bash
set -euo pipefail

REPO=4sq4r/serendipity

if [ $# -ne 4 ]; then
  echo "usage: create-story.sh <epic-number> <title> <area-labels> <body-file>" >&2
  exit 1
fi

epic="$1"
title="$2"
area_labels="$3"
body_file="$4"

epic_labels=$(gh issue view "$epic" -R "$REPO" --json labels --jq '[.labels[].name] | join(",")')
case ",$epic_labels," in
  *,epic,*) ;;
  *) echo "issue #$epic is not an epic" >&2; exit 1 ;;
esac

epic_title=$(gh issue view "$epic" -R "$REPO" --json title --jq .title)

url=$(gh issue create -R "$REPO" --title "[$epic_title] $title" --label "story,$area_labels" --body-file "$body_file")
number="${url##*/}"
id=$(gh api "repos/$REPO/issues/$number" --jq .id)
gh api -X POST "repos/$REPO/issues/$epic/sub_issues" -F sub_issue_id="$id" >/dev/null
echo "$number"

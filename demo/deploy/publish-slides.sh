#!/usr/bin/env bash
# Build the deck on this laptop and ship it to the server. Run from anywhere.
#   demo/deploy/publish-slides.sh
set -euo pipefail
root="$(cd "$(dirname "$0")/../.." && pwd)"
cd "$root"
pnpm slidev build
tar -C dist -czf - . | ssh giftson 'set -e; d=/root/fx-seminar-todo-app/demo/deploy/slides/dist; mkdir -p "$d.new" && tar -C "$d.new" -xzf - && rm -rf "$d" && mv "$d.new" "$d" && echo "deck shipped: $(ls "$d" | wc -l) entries"'
echo "https://fx-presentation.giftson.org"

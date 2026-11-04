#!/usr/bin/env bash
set -e
cd -- "$(dirname -- "$0")"
mkdir -p out
javac -encoding UTF-8 --release 17 -d out @fuentes.txt
java -Dfile.encoding=UTF-8 -cp out vista.Principal "$@"

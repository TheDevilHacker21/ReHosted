#!/bin/bash
cd /home/quinn/Paescape
git reset --hard origin/main  # Ensures a clean sync
git pull origin main
./0build.sh
pid=$!

wait $pid
./0run.sh   # Replace with your game server's startup command
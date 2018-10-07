#!/bin/zsh

echo "tag name:\n"
read tag
docker build -t reverse_polish_notation_calculator:$tag .
#!/bin/bash

kernel=$(uname -s)

if [[ "$kernel" == "Linux" ]]; then
    echo "linux"
elif [[ "$kernel" == "Darwin" ]]; then
    echo "macOS"
elif [[ "$kernel" == "cygwin" || "$kernel" == "msys" ]]; then
    echo "windows"
else
    exit 1
fi
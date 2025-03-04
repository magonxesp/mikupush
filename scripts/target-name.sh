#!/bin/bash

kernel=$(uname -s)
arch=$(uname -m)

if [[ "$kernel" == "Linux" ]]; then
    echo "linux_$arch"
elif [[ "$kernel" == "Darwin" ]]; then
    echo "macOS_$arch"
elif [[ "$kernel" == "cygwin" || "$kernel" == "msys" ]]; then
    echo "windows_$arch"
else
    exit 1
fi
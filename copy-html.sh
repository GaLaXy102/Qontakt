#!/bin/bash

for app in "$@"; do
  rm -rf deploy/"$app"/content
  mkdir -p deploy/"$app"/content
  cp -rf "$app"/out/* deploy/"$app"/content/
done
#!/bin/bash

for app in "$@"; do
  cp -f "$app"/target/app-*.jar deploy/"$app"/app.jar
done
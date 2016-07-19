#!/usr/bin/env bash

nohup python3 src/webapp_flask.py -r | tee build.out

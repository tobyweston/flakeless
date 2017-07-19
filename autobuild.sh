#!/usr/bin/env bash

# requires: brew install entr

source ./cleanElm.sh

cd elm

ls `find . -name \*.elm -not -path \*elm-stuff*  -print` | entr sh -c 'clear; rm ../src/main/webapp/elm/flakeless.js; elm-make `find . -name \*.elm -not -path \*elm-stuff*  -print` --output ../src/main/webapp/elm/flakeless.js'

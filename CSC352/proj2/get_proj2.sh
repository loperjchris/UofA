#!/bin/bash

ME=$(whoami)
BASE=~jmisurda/original
DIR=${BASE}/csc352-proj2

cp ${DIR}/${ME}_1 .
cp ${DIR}/${ME}_2 .
cp ${DIR}/${ME}_3 .
cp ${BASE}/disable_aslr.sh .

echo "Done"

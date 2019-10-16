#!/bin/bash

for i in `seq 8112 65535`
do
	url="https://localhost:$i/internal"
	#echo "[-] Testing $url"
	./exploit.py $url
done

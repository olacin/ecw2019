#!/usr/bin/python

import pexpect
import sys
import curses

for line in open('rockyou.txt','r').readlines():
    #print(line.rstrip())
    tryString = 'Trying out {} '.format(line.rstrip())
    #sys.stdout.write('\r%s' % tryString)
    #sys.stdout.flush()
    print(tryString)
    child = pexpect.spawn('7z e ntfs.7z -p ' + line.rstrip())
    resp = ''
    for item in child:
        resp += item
    child.close()
    if not 'Wrong password' in resp and not 'No files to process' in resp:
        print('\n###################################\n')
        print(resp)
        print('\n###################################\n')
        print('[+] Password : ' + line.rstrip())
        f = open('pass.txt','a')
        f.write('[+] Password : ' + line)
        f.close()
        break

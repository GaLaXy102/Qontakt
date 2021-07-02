"""
Handlers for Qontakt CryptoUI

@author: Konstantin Köhring <konsti@galaxy102.de>
"""

from Crypto.Cipher import AES
from Crypto.Cipher import PKCS1_v1_5
from Crypto.PublicKey import RSA
from Crypto import Random
from Crypto.Hash import SHA3_512


class HybridDecryptor:
    """
    Decryptor for Hybrid Encryption (AES wrapped by RSA)
    Decryption Mode: Decrypt AES key with RSA/NONE/PKCS1Padding from first bytes
                     Decrypt Content with AES/ECB/PKCS5Padding from remainder
    """

    @staticmethod
    def decrypt(private_key: bytes, data: bytes, passphrase=None) -> bytes:
        """
        Decrypt data
        :param private_key: private RSA key
        :param data: encrypted data
        :param passphrase: passphrase for RSA key
        :return: decrypted data
        """
        rsa_key = RSA.import_key(private_key, passphrase)
        rsa_key_len = int(rsa_key.size_in_bits() / 8)
        aes_key_enc = data[:rsa_key_len]
        # See https://pycryptodome.readthedocs.io/en/latest/src/cipher/pkcs1_v1_5.html
        sentinel = Random.new().read(2048 + SHA3_512.digest_size)
        aes_key = PKCS1_v1_5.new(rsa_key).decrypt(aes_key_enc, sentinel)
        return AES.new(aes_key, AES.MODE_ECB).decrypt(data[rsa_key_len:])

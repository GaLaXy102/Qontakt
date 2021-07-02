from qontakt.crypto.util import HybridDecryptor

if __name__ == '__main__':
    with open("../test.qenc", 'rb') as ciphertext:
        with open("../test.pem", 'rb') as key:
            with open("../test.pdf", 'wb') as target:
                target.write(HybridDecryptor.decrypt(key.read(), ciphertext.read(), "blublablub"))

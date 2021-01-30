package app.qontakt.crypto.pki;

import app.qontakt.crypto.RSACryptoService;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PublicKeyData {

    private static final String sampleRsaPublicKeyPem = """
            -----BEGIN PUBLIC KEY-----
            MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAtwCYgzlUEYAD3NwFAvv8
            6tMbPlX9SwDzep4o4i5eV+Vn8l+5DV34lJ/fusNxPB1eAvW71Z44hv8kZ0eefXWH
            yR3OsjQBGFdYsRT9rme4nUx6gqFJ/WupEyYguABRv30Bxyd9tArYcnPTm/MgZAsb
            JLZGdSI2pIqxNTk4nsb9RvTtciXgIIbqcdriTLc2K/UFDElxjk3ykA5YgDDBAMEf
            hTiwIzXCU+Q5rTLQ398VNXRz+MSlgGmjS5uWYgpOCGrg2kCqy0ogMunABwRYvn3s
            4LakT/yCeWCQPPspwzX+FFzPoH4229cCaWUDQ1JncraRPVSQiiM6nokWI/bJ3mz5
            C5ESgo+6T5aRNRHeWAv+Dyur9qnRubvgymnB6eUJdfXwaxGGKosWGDa+F/mL+2ME
            xfHgTP/Zv/HJx0c7n+lLle9ADZhVKs6IsWsJbrNRmUS4nXE3ROvK6e46Y4lAgWyw
            4yL4vyhcm6GTYCAKan+rziSqVpuNAkCHCuYU0DOD6upLd7OnijaB8C4hbhbDoxQP
            YH/sr9YorLXqw3GEEBLxDx7hFGIXW33nMn4+ek9la/PK+/6ivE/3o67RqX3jIOtA
            H4m+3XHKscV50F6w6qHXGTH9/grVb9znJFsCrWeaKt99Yo73Fw61ncuTXn3ZN5jl
            wGtwQy+N4n1x04LlLL2CqwcCAwEAAQ==
            -----END PUBLIC KEY-----
            """;

    private static final SavedRSAPublicKey sampleRsaPublicKey =
            new SavedRSAPublicKey(RSACryptoService.readPublicKey(
                    new ByteArrayInputStream(sampleRsaPublicKeyPem.getBytes(StandardCharsets.UTF_8))),
                    "Qontakt Sample Key (Do not use this!)"
            );

    public static final List<SavedRSAPublicKey> allRsaPublicKeys = List.of(sampleRsaPublicKey);
}

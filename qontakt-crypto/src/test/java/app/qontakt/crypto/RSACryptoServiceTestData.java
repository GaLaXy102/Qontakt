package app.qontakt.crypto;

public class RSACryptoServiceTestData {

    public static String privateKeyString = """
            -----BEGIN RSA PRIVATE KEY-----
            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
            -----END RSA PRIVATE KEY-----""";

    public static String publicKeyString = """
            -----BEGIN PUBLIC KEY-----
            MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA2m2lC5yBgydC0t4m6Sd9
            S9SnIuq71piAo6AkDfEr9zZARW630fMRWD8CZUtkyVmqO1MSAo/L6qu+v5Yifpa8
            BKlNJS+jBO01fBm43ff8I0mPrghlrwV6o1b+aUoRU0zfiojTAFZlmX+B/JEdBQc8
            xDTOzpG1UTwqiLS3Kn214V0b8q2InDA14feGpdqcNj4wynxfrU/HXPObQp8eYDzb
            1gZWug8PjuBmNlBM12m1SrgHCld5FgGwyB1njaHmdCOP1v6xJ1+aU5M2b2FlyNoH
            ZBXXiq6jKLusatJP+VmB2qs6Twbnm9jIq5ZDToZr6SV7P6/SF/Xt5qTLmi2uYGDe
            UbXLxO5yG8p6AHS7qXjXSz2FYE/UIC7jzkIsMQlh5eQVr88dsXZDjYpvpt64RNKO
            oVcFSq5kLZaq0CwDQ3e0E9F3HmEH89yS+b3cKNs+lcYRWonLHP1Bhrgk19JDl9pt
            VSU4D08dYY+1G5zP0hQYfaYoSzW1p7mhlPEC9z+NcakhsYh3f0bNj80Y6kJFjMnM
            KCMBaJlzmZo/aQ2Ao+bjpzvC5WIeZmmDysEiqdj0LvpDr4XPkmLah01nOyAJZWnq
            zjIAFwHT/24WUrbMNeWw0e/MCTe2fI5POEBJP0OJlVV1Ob4aPOjh1/vScB1e5xZg
            zaD3bADk8mk4uK2zDRlGZMUCAwEAAQ==
            -----END PUBLIC KEY-----""";

    public static String encryptedPrivateKeyString = """
            -----BEGIN RSA PRIVATE KEY-----
            Proc-Type: 4,ENCRYPTED
            DEK-Info: DES-EDE3-CBC,F535F6A52490B7C4
                        
            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
            -----END RSA PRIVATE KEY-----
            """;

    public static String encryptedPublicKeyString = """
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

    public static String passphrase = "blublablub";
}

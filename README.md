

# Cipher

Symmetrical data encryption/decryption tool using AES256, with random salt and IV. Requires Java 11 or above, tested to be working on JDK 16.0.2

## Example (in Powershell)

Encryption:
```
PS C:\> $env:CIPHER_PASSWORD = 'mypassword'
PS C:\> java -jar .\cipher-3.0.jar -o mydata.encrypted .\mydata.txt
```

Decryption:
```
PS C:\> java -jar .\cipher-3.0.jar -d -o mydata.txt.decrypted .\mydata.encrypted

PS C:\PRIVATE> fc.exe .\mydata.txt.decrypted .\mydata.txt
Comparing files .\mydata.txt.decrypted and .\mydata.txt
FC: no differences encountered

```

## Usage

```
cipher.jar 1.0
Usage: cipher [options] [<Input File>]

  <Input File>             The file to encrypt. Default from STDIN if not given.
  -o, --out <Output File>  (Required) Output file for encrypted data. Use '-' to indicate STDOUT.
  -p, --password-file <value>
                           The file where cipher password is read. The first line of the file is used as password.
                           Alternatively, the password can be passed using environment variable 'CIPHER_PASSWORD'.
  -d, --decrypt            Decryption mode. The cipher runs in encryption mode by default.
  -q, --quiet              Do not show progress on console when not writing to STDOUT.
  --help                   prints this usage text


The data is encrypted with AES-256-GCM with randomly generated salt.
Generally, the same version of cipher.jar must be used in both encryption and decryption.

Examples:

  java -jar cipher.jar -p passwd_file -o encrypted input_file

  CIPHER_PASSWORD=mypassword java -jar cipher.jar -d -o decrypted encrypted
```

## Build

```
gradle build
```

## LICENCE

MIT

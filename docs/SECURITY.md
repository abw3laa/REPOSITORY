# Security baseline

The security model is fail-closed: recording must never silently fall back to an unsafe capture path.

1. Recordings are local by default.
2. No cloud upload is part of 1.0.0.
3. Recording files use AES-GCM encryption with an AES key held by Android Keystore.
4. Recording filenames use random identifiers; phone numbers must not appear in filenames or logs.
5. The app does not request READ_CALL_LOG. That permission is restricted on Google Play and is not required for the current call-state boundary.
6. Sensitive IPC with a privileged recorder component must be authenticated before any recorder command is accepted.
7. The privileged component must expose only a narrow command protocol; arbitrary shell execution is forbidden.
8. The recorder must fail closed when authentication or capability checks fail.
9. Logs must not contain call audio, encryption keys, authentication material, or full phone numbers.
10. Full device backup of recordings is disabled until secure export is designed.
11. Encryption metadata and future call metadata must be treated as sensitive local data.
12. Any future update installer must verify release authenticity before installation.

## Current implementation

- `EncryptedRecordingStore` encrypts recording bytes with AES/GCM/NoPadding.
- Each file receives a fresh 12-byte random nonce.
- The encryption key is generated and retained in Android Keystore.
- The encrypted file contains a versioned header and GCM authentication tag.
- Decryption fails if the header, version, nonce, or authentication tag is invalid.

## Privileged recording boundary

The project may support a no-root recorder path using user-enabled Android debugging capabilities on compatible devices. This is not treated as a universal Android capability.

The app must never:
- request privileged signature-only audio permissions;
- use AccessibilityService as a call-recording workaround;
- execute arbitrary commands supplied by the user, network, or external content;
- claim two-way call recording support before a real-device capability test verifies it.


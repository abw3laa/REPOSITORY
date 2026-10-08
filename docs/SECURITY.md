# Security baseline

1. Recordings are local by default.
2. No cloud upload is part of 1.0.0.
3. Recording files will be encrypted at rest using Android Keystore-backed keys.
4. Sensitive IPC with a privileged recorder component must be authenticated.
5. The recorder must fail closed when authentication or capability checks fail.
6. Logs must not contain call audio, encryption keys or authentication material.
7. Full device backup of recordings is disabled until secure export is designed.
8. Privileged commands must use a narrowly scoped protocol.

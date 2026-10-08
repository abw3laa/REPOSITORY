# Call Recorder architecture

## Scope of 1.0.0

The first release targets cellular voice calls only, without root access. WhatsApp and other VoIP applications are out of scope.

## Core rule

The application must never claim that two-way recording is supported until a device self-test verifies the selected capture path.

## Layers

- call: telephony state and call sessions.
- recording: device-independent recording contract and capability tests.
- privileged: future embedded-ADB / shell-daemon integration.
- storage: recording metadata, files, retention and encryption.
- security: Android Keystore, authentication and secure IPC.
- ui: Compose screens only.
- updates: release metadata and signed update verification.

## Recording flow

PHONE_STATE -> CallSessionManager -> Capability check -> RecordingEngine -> encrypted local file -> metadata repository

The privileged recorder is intentionally not implemented in the foundation commit. It will be added only after its Android-version and OEM compatibility boundaries are documented.

## Compatibility

Android API level, OEM, audio route, Bluetooth state and shell/ADB capability are runtime capabilities, not universal guarantees.

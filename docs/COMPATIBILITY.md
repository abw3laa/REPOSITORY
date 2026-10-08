# Compatibility strategy

This project does not assume that Android exposes two-way cellular call audio to an ordinary application.

## Baseline

- minSdk 26.
- Android/Kotlin/Compose.
- Cellular voice calls only in 1.0.0.
- No root.
- No AccessibilityService workaround.
- No WhatsApp/VoIP recording in 1.0.0.
- Real-device testing is mandatory before declaring an engine supported.

## Capability matrix

The runtime diagnostic model tracks:

- Android API level.
- Manufacturer and model.
- `READ_PHONE_STATE` permission.
- `RECORD_AUDIO` permission.
- notification permission where applicable.
- recorder-engine readiness.
- verified two-way call audio.

A device is not considered "supported" merely because permissions are granted.

## Wireless debugging / privileged path

A future no-root privileged recorder path may use user-enabled Wireless debugging / ADB capabilities on compatible Android versions. Android's documented wireless-debugging flow requires the user to enable Wireless debugging and pair the device with a workstation or approved pairing flow; this is not equivalent to a normal application permission. cite-placeholder

The recorder implementation must therefore:

1. Detect whether the required capability exists.
2. Obtain explicit user authorization where the platform requires it.
3. Establish authenticated IPC with the recorder component.
4. Start only the narrow recording command.
5. Stop and tear down the privileged component after the call.
6. Report unsupported instead of silently recording one side only.

## Foreground-service constraints

Android 14+ requires foreground-service types to be declared. The `phoneCall` type has prerequisites tied to `MANAGE_OWN_CALLS` or the default dialer role, so it must not be declared merely to bypass platform restrictions. A microphone foreground service also has while-in-use permission restrictions.

The final service design must be validated against the exact Android API level and distribution channel before release.

## Release rule

No marketing or UI label may claim "works on all Android phones". Support must be reported per device/OS/capture engine after self-test.

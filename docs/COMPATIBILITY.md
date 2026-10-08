# Device compatibility

The application will maintain a capability matrix instead of assuming one recording method works on every Android device.

The self-test will eventually report:

- Android API level
- manufacturer and model
- Wireless Debugging / ADB availability
- privileged daemon availability
- selected audio capture path
- microphone capability
- call-audio capability
- whether both directions are audible
- Bluetooth routing result
- screen-lock/background result

A device is considered two-way compatible only when the complete self-test succeeds.

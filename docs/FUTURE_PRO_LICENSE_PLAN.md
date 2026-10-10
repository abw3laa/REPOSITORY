# Future Pro Licensing Plan (Deferred)

**Status:** Discussion/design decision only — NOT approved for implementation in the current release.  
**Earliest review:** In one month or later, when the core call-recording functionality is stable.  
**Scope of this change:** Documentation only. Do not add Pro UI, licensing code, payment flows, server endpoints, or feature gates now.

## Product decision

The app will initially focus on its core personal call-recording functionality. Pro licensing is a future-release idea and must not delay or complicate the current release.

When the feature is revisited, support both:
- A permanent Pro license.
- Monthly and yearly subscriptions.

## Activation-key and device-transfer rules

1. A purchase activation key is redeemable once to create/claim a license.
2. Redeeming that key later on a replacement phone must not create a second license or require the customer to buy another key.
3. Only one device may be the license's active device on the server at a time.
4. The customer may transfer the license to another phone at any time. Do not impose a waiting period or transfer cooldown.
5. On transfer, the server atomically changes the active device from the old phone to the new phone and revokes the old phone's license association.
6. A future account/dashboard may be added to help customers recover and transfer licenses if the old phone is lost or broken.

## Offline behavior and revocation

- A successfully activated device must be able to use its locally cached license while offline.
- After a transfer, the old phone cannot learn about revocation until it reconnects, or until its locally cached proof expires. Therefore, simultaneous use cannot be prevented absolutely while the old phone remains offline.
- When the old phone reconnects, it must refresh license state, detect revocation, and disable Pro features automatically.
- Use a bounded offline proof/grace policy for subscriptions and revocation checks. The exact duration is a future design decision; do not invent or implement it now.
- Avoid disrupting an active call-recording session if a license refresh or expiry occurs mid-call. Define exact behavior during implementation.
- Do not claim that offline revocation is instantaneous or tamper-proof.

## Data safety

- Never delete or corrupt the user's existing recordings when a license is transferred, revoked, or expires.
- License checks must eventually protect the actual paid operations, not only hide Pro buttons in the UI.
- Keep license-signing secrets on the trusted issuing service; the app should only contain the public verification key if signed offline license proofs are used.
- Do not embed server-side secrets in the Android app.

## Future implementation checklist

Before implementation:
- Inspect the current repository and architecture; do not assume files or services exist.
- Define license and device identifiers, signed license proof format, server-side transfer transaction, subscription expiry, offline grace, and recovery flow.
- Decide how to authenticate the customer when transferring a license without access to the old phone.
- Add tests for one active device, repeated transfers without cooldown, one-time key redemption, offline operation, revocation after reconnect, subscription expiry, and preservation of recordings.
- Implement on a task branch, run relevant tests, and verify behavior before merging.

## Non-goals for the current release

- No Pro paywall or locked features.
- No activation-key entry or redemption UI.
- No licensing backend or admin dashboard.
- No subscription or payment integration.
- No changes to the current recording functionality solely to prepare for licensing.

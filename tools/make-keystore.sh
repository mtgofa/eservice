#!/usr/bin/env bash
# One-time: create the release signing key + keystore.properties (both git-ignored),
# and print the values to paste into GitHub → Settings → Secrets → Actions for CI releases.
# KEEP keystore/ SAFE: losing it means installed apps can never update again.
set -euo pipefail
cd "$(dirname "$0")/.."

[ -f keystore/eservice-release.jks ] && { echo "keystore/eservice-release.jks already exists" >&2; exit 1; }
read -rsp "Keystore password (min 6 chars): " PASS; echo
mkdir -p keystore
keytool -genkeypair -v -keystore keystore/eservice-release.jks -alias eservice \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -storepass "$PASS" -keypass "$PASS" -dname "CN=eService, O=mtgofa"
cat > keystore.properties <<PROPS
storeFile=keystore/eservice-release.jks
storePassword=$PASS
keyAlias=eservice
keyPassword=$PASS
PROPS

base64 -w0 keystore/eservice-release.jks > keystore/eservice-release.jks.b64

echo
echo "GitHub Actions secrets:"
echo "  KEYSTORE_PASSWORD = (the password you typed)"
echo "  KEY_ALIAS         = eservice"
echo "  KEY_PASSWORD      = (the password you typed)"
echo "  KEYSTORE_BASE64   = contents of keystore/eservice-release.jks.b64"

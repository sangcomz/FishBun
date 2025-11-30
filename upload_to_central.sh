#!/bin/bash

# FishBun Upload to Central Portal API Script
# Based on https://central.sonatype.org/publish/publish-portal-api/

set -e

# Load credentials from local.properties if not set as environment variables
if [ -z "$CENTRAL_TOKEN" ]; then
    if [ -f "local.properties" ]; then
        CENTRAL_TOKEN=$(grep "^centralToken=" local.properties | cut -d'=' -f2)
    fi
fi

if [ -z "$SIGNING_PASSWORD" ]; then
    if [ -f "local.properties" ]; then
        SIGNING_PASSWORD=$(grep "^signingPassword=" local.properties | cut -d'=' -f2)
    fi
fi

# Check required token
if [ -z "$CENTRAL_TOKEN" ]; then
    echo "Error: CENTRAL_TOKEN is required"
    echo "Please add it to local.properties:"
    echo "  centralToken=username:password"
    echo ""
    echo "Get your credentials from: https://central.sonatype.com/account"
    exit 1
fi

# Configuration
GROUP_ID="io.github.sangcomz"
ARTIFACT_ID="fishbun"
VERSION=$(grep "versionName" settings.gradle | cut -d"'" -f2)
BASE_URL="https://central.sonatype.com/api/v1"

if [ -z "$VERSION" ]; then
    echo "Error: Could not determine version from settings.gradle"
    exit 1
fi

echo "Uploading FishBun $VERSION to Central Portal..."

# Central Portal API requires username:password to be Base64 encoded
# Token format: username:password -> Base64 encoded
if [[ "$CENTRAL_TOKEN" == *":"* ]]; then
    # Token is in username:password format, encode it
    USER_TOKEN=$(printf "%s" "$CENTRAL_TOKEN" | base64)
    echo "Encoding username:password token to Base64..."
else
    # Token might already be Base64 encoded
    USER_TOKEN="$CENTRAL_TOKEN"
fi

# Verify token authentication
echo "Testing Central Portal authentication..."
TEST_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" \
    -H "Authorization: Bearer $USER_TOKEN" \
    "$BASE_URL/publisher/status" 2>/dev/null || echo "HTTP_CODE:000")

TEST_CODE=$(echo "$TEST_RESPONSE" | tail -1 | cut -d':' -f2)
if [ "$TEST_CODE" = "200" ] || [ "$TEST_CODE" = "404" ]; then
    echo "Token authentication successful"
elif [ "$TEST_CODE" = "401" ]; then
    echo "Token authentication failed"
    echo ""
    echo "Token format should be: username:password"
    echo "Get your credentials from: https://central.sonatype.com/account"
    echo "Then update centralToken in local.properties"
    exit 1
fi

# Set Java 17 for build compatibility
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home

# Ensure GPG is in PATH (for Gradle signing)
export PATH="/opt/homebrew/bin:$PATH"

# Set GPG TTY for non-interactive signing
export GPG_TTY=$(tty)

# Configure GPG for non-interactive use
echo "allow-loopback-pinentry" >> ~/.gnupg/gpg-agent.conf 2>/dev/null || true
gpgconf --kill gpg-agent 2>/dev/null || true

# Upload GPG public key to keyservers (if not already uploaded)
echo "Ensuring GPG public key is available on keyservers..."
# Use both short and full key ID for better compatibility
gpg --keyserver keyserver.ubuntu.com --send-keys 55C96C4310378A95 2>/dev/null || true
gpg --keyserver keys.openpgp.org --send-keys 55C96C4310378A95 2>/dev/null || true
echo "GPG key uploaded. Note: It may take a few minutes to propagate across keyservers."

# Build the project and generate artifacts (without signing first)
echo "Building project..."
./gradlew clean :FishBun:build :FishBun:publishToMavenLocal -x test -PskipSigning=true

# Find the generated artifacts
LOCAL_REPO="$HOME/.m2/repository"
ARTIFACT_PATH="$LOCAL_REPO/io/github/sangcomz/fishbun/$VERSION"

if [ ! -d "$ARTIFACT_PATH" ]; then
    echo "Error: Artifacts not found at $ARTIFACT_PATH"
    echo "Make sure the build completed successfully"
    exit 1
fi

# Create deployment bundle with proper directory structure
BUNDLE_DIR="bundle"
rm -rf $BUNDLE_DIR
mkdir -p "$BUNDLE_DIR/io/github/sangcomz/fishbun/$VERSION"

echo "Preparing artifacts..."
# Copy all artifacts to proper path
cp "$ARTIFACT_PATH"/* "$BUNDLE_DIR/io/github/sangcomz/fishbun/$VERSION/" 2>/dev/null || true

echo "Files in bundle directory:"
ls -la "$BUNDLE_DIR/io/github/sangcomz/fishbun/$VERSION/"

# Generate MD5, SHA1 checksums and signatures for all files
echo "Generating checksums and signatures..."
cd "$BUNDLE_DIR/io/github/sangcomz/fishbun/$VERSION"

# Process all main artifact files
for file in *.pom *.aar *.jar *.module; do
    if [ -f "$file" ]; then
        # Generate checksums
        md5 -q "$file" > "${file}.md5"
        shasum -a 1 "$file" | cut -d' ' -f1 > "${file}.sha1"

        # Generate GPG signature if not already exists
        if [ ! -f "${file}.asc" ]; then
            echo "Signing $file..."
            echo "$SIGNING_PASSWORD" | gpg --batch --yes --passphrase-fd 0 --pinentry-mode loopback --armor --detach-sign --default-key 10378A95 "$file"
        fi
    fi
done
cd - > /dev/null

# Create bundle zip
BUNDLE_ZIP="fishbun-${VERSION}-bundle.zip"
cd $BUNDLE_DIR
zip -r "../$BUNDLE_ZIP" .
cd ..

echo "Created bundle: $BUNDLE_ZIP"

# Upload to Central Portal
echo "Uploading to Central Portal..."

RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" \
    -X POST \
    -H "Authorization: Bearer $USER_TOKEN" \
    -F "bundle=@$BUNDLE_ZIP" \
    "$BASE_URL/publisher/upload")

HTTP_CODE=$(echo "$RESPONSE" | tail -1 | cut -d':' -f2)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "201" ]; then
    echo "Upload successful!"
    echo "Response: $BODY"

    # Extract deployment ID if available
    DEPLOYMENT_ID=$(echo "$BODY" | grep -o '"deploymentId":"[^"]*' | cut -d'"' -f4)
    if [ -n "$DEPLOYMENT_ID" ]; then
        echo "Deployment ID: $DEPLOYMENT_ID"
        echo "Check status at: https://central.sonatype.com/publishing/deployments"
    fi
else
    echo "Upload failed with HTTP $HTTP_CODE"
    echo "Response: $BODY"
    exit 1
fi

# Clean up
rm -rf $BUNDLE_DIR
rm -f "$BUNDLE_ZIP"

echo "Upload completed! Check the Central Portal for publishing status."
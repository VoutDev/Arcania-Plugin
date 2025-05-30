#!/bin/bash

cd "$(dirname "$0")" 
# Configuration
VERSION="1.21.5"
PROJECT="paper"
API_URL="https://api.papermc.io/v2/projects/${PROJECT}/versions/${VERSION}"
DOWNLOAD_DIR="../server/paper"
OUTPUT_JAR="${DOWNLOAD_DIR}/paper.jar"

# Ensure the download directory exists
mkdir -p "${DOWNLOAD_DIR}"

# Fetch latest build number
echo "Fetching latest build for Paper $VERSION..."
BUILD=$(curl -s "${API_URL}/builds" | jq '.builds | sort_by(.build) | last | .build')

if [ -z "$BUILD" ]; then
  echo "Failed to get latest build for version $VERSION."
  exit 1
fi

echo "Latest build: $BUILD"

# Compose download URL
JAR_NAME="paper-${VERSION}-${BUILD}.jar"
JAR_URL="${API_URL}/builds/${BUILD}/downloads/${JAR_NAME}"

# Download the jar
echo "Downloading $JAR_NAME..."
curl -o "${OUTPUT_JAR}" -L "${JAR_URL}"

if [ $? -ne 0 ]; then
  echo "Download failed."
  exit 1
fi

echo "Downloaded to ${OUTPUT_JAR}"

echo "Paper installed successfully, make sure you run the start.sh script to create the necessary plugins folder before building the plugin."
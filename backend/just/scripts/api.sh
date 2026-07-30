#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)
PROJECT_ROOT=$(cd -- "$SCRIPT_DIR/../.." && pwd)

API_URL="${API_URL:-http://localhost:8081}"

HEADERS=()

parse_arguments() {
    METHOD=$1
    API_PATH=$2
    EXTRACT=
    OUTPUT=

    if [[ ${3:-} == "--extract" ]]; then
        EXTRACT=$4
        OUTPUT=$5
    fi
}

configure_headers() {
    if [[ -f "$PROJECT_ROOT/.jwt" ]]; then
        HEADERS+=(
            -H "Authorization: Bearer $(<"$PROJECT_ROOT/.jwt")"
        )
    fi
}

print_response() {
    local response=$1

    if [[ -s "$response" ]]; then
        printf 'Response:\n'
        jq . < "$response"

        if [[ -n "$EXTRACT" ]]; then
            jq -r "$EXTRACT" < "$response" > "$OUTPUT"
        fi
    fi
}

request() {
    local response
    local status

    response=$(mktemp)

    status=$(
        curl \
            -sS \
            -o "$response" \
            -w '%{http_code}' \
            -X "$METHOD" \
            "${HEADERS[@]}" \
            "$@" \
            "${API_URL}${API_PATH}"
    )
    print_response "$response"
    rm "$response"

    if (( status >= 400 )); then
        exit 1
    fi
}

request_json() {
    local request

    request=$(mktemp)
    cat > "$request"

    printf 'Request:\n'
    jq . < "$request"

    request \
        -H "Content-Type: application/json" \
        --data-binary @"$request"

    rm "$request"
}

parse_arguments "$@"
configure_headers

if [[ -t 0 ]]; then
    request
else
    request_json
fi

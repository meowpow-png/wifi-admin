#!/usr/bin/env bash

set -euo pipefail

grafana_url="${GRAFANA_URL:-http://localhost:3000}"
grafana_username="${GRAFANA_USERNAME:-admin}"
grafana_password="${GRAFANA_PASSWORD:-admin}"

grafana_api() {
    curl \
        --silent \
        --show-error \
        --fail \
        --user "${grafana_username}:${grafana_password}" \
        "${grafana_url}$1"
}

list_dashboards() {
    grafana_api "/api/search?type=dash-db"
}

dashboard_entries() {
    jq -r '.[] | [.uid, .title] | @tsv'
}

export_dashboard() {
    local uid="$1"
    local output="$2"

    grafana_api "/api/dashboards/uid/${uid}" \
        | jq '.dashboard | del(.id)' \
        > "${output}"
}

print_dashboards() {
    echo "Dashboard API returned $(echo "${dashboard_list}" | jq 'length') dashboards"

    echo "Available dashboards:"
    echo "${dashboard_list_entries}"
}

export_dashboards() {
    echo "Exporting dashboards..."

    echo "${dashboard_list_entries}" \
        | while IFS=$'\t' read -r uid title; do
            local output="docker/grafana/dashboards/${uid}.json"

            echo "Exporting ${title} (${uid}) -> ${output}"
            export_dashboard "${uid}" "${output}"
            echo "Done: ${output}"
        done

    echo "Dashboard export complete."
}

echo "Grafana URL: ${grafana_url}"
echo "Fetching dashboard list..."

dashboard_list="$(list_dashboards)"
dashboard_list_entries="$(echo "${dashboard_list}" | dashboard_entries)"

print_dashboards
export_dashboards

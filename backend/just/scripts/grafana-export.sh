#!/usr/bin/env bash

set -euo pipefail

grafana_url="${GRAFANA_URL:-http://localhost:3000}"
grafana_username="${GRAFANA_USERNAME:-admin}"
grafana_password="${GRAFANA_PASSWORD:-admin}"

echo "Grafana URL: ${grafana_url}"
echo "Fetching dashboard list..."

dashboard_list="$(
curl \
  --silent \
  --show-error \
  --fail \
  --user "${grafana_username}:${grafana_password}" \
  "${grafana_url}/apis/dashboard.grafana.app/v1/namespaces/default/dashboards"
)"

echo "Dashboard API returned $(echo "${dashboard_list}" | jq '.items | length') dashboards"

echo "Available dashboards:"
echo "${dashboard_list}" | jq -r '
  .items[]
  | [
      .metadata.name,
      (.metadata.annotations["grafana.app/managedBy"] // "<none>"),
      (.metadata.annotations["grafana.app/sourcePath"] // "<none>")
    ]
  | @tsv
'

echo "Dashboards matching classic-file-provisioning:"

echo "${dashboard_list}" \
| jq -r '
    .items[]
    | select(.metadata.annotations["grafana.app/managedBy"] == "classic-file-provisioning")
    | [.metadata.name, .metadata.annotations["grafana.app/sourcePath"]]
    | @tsv
' \
| while IFS=$'\t' read -r name source_path; do
    echo "Matched: ${name} (${source_path})"

    output="docker/grafana/dashboards/$(basename "${source_path}")"
    echo "Exporting ${name} -> ${output}"

    curl \
        --silent \
        --show-error \
        --fail \
        --user "${grafana_username}:${grafana_password}" \
        "${grafana_url}/apis/dashboard.grafana.app/v1/namespaces/default/dashboards/${name}" \
    | jq 'del(
        .metadata.uid,
        .metadata.resourceVersion,
        .metadata.generation,
        .metadata.creationTimestamp,
        .status
    )' \
    > "${output}"

    echo "Done: ${output}"
done

echo "Dashboard export complete."

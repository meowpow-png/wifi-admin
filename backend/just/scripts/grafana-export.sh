#!/usr/bin/env bash

set -euo pipefail

grafana_url="${GRAFANA_URL:-http://localhost:3000}"
grafana_username="${GRAFANA_USERNAME:-admin}"
grafana_password="${GRAFANA_PASSWORD:-admin}"

curl \
  --silent \
  --show-error \
  --fail \
  --user "${grafana_username}:${grafana_password}" \
  "${grafana_url}/apis/dashboard.grafana.app/v1/namespaces/default/dashboards" \
| jq -r '
    .items[]
    | select(.metadata.annotations["grafana.app/managedBy"] == "classic-file-provisioning")
    | [.metadata.name, .metadata.annotations["grafana.app/sourcePath"]]
    | @tsv
' \
| while IFS=$'\t' read -r name source_path; do
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
done

echo "Dashboard export complete."

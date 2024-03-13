output "cluster_endpoint" {
  value       = google_container_cluster.choresync.endpoint
  description = "The endpoint for the GKE cluster"
}

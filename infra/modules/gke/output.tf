output "choreshare_cluster_name" {
  value = google_container_cluster.choreshare.name
  description = "The name of the GKE cluster"
  
}

output "choreshare_cluster_endpoint" {
  value       = google_container_cluster.choreshare.endpoint
  description = "The endpoint for the GKE cluster"
}

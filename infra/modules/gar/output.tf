output "choreshare_registry_name" {
  value = google_artifact_registry_repository.choreshare.name
  description = "The name of the GCR repository"
}

output "choreshare_registry_id" {
  value = google_artifact_registry_repository.choreshare.id
  description = "The URL of the GCR repository"
}

output "choreshare_registry_location" {
  value = google_artifact_registry_repository.choreshare.location
  description = "The location of the GCR repository"
}
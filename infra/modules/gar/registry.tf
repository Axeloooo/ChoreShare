# Google Artifact Registry (GAR) repository module

resource "google_artifact_registry_repository" "my-repo" {
  location      = var.REGION
  repository_id = var.REPOSITORY_ID
  description   = "My Docker repository"
  format        = "DOCKER"
}

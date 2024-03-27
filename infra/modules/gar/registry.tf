# Google Artifact Registry (GAR) repository module

resource "google_artifact_registry_repository" "choreshare" {
  location      = var.REGION
  repository_id = var.REPOSITORY_ID
  description   = "ChoreShare repository"
  format        = "DOCKER"
}

resource "google_container_cluster" "choreshare" {
  name     = var.CLUSTER_NAME
  location = var.REGION

  enable_autopilot    = true
  deletion_protection = false
}


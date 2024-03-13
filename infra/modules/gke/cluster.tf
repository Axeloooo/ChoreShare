resource "google_container_cluster" "choresync" {
  name     = var.CLUSTER_NAME
  location = var.REGION

  enable_autopilot    = true
  deletion_protection = false
}


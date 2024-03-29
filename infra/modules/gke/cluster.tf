resource "google_container_cluster" "choreshare" {
  name     = var.CLUSTER_NAME
  location = var.REGION

  remove_default_node_pool = true
  initial_node_count       = 1
  deletion_protection = false
}

resource "google_container_node_pool" "choreshare" {
  name       = var.NODE_POOL_NAME
  location   = var.REGION
  cluster    = google_container_cluster.choreshare.name
  node_count = 1

  node_config {
    preemptible  = true
    machine_type = var.MACHINE_TYPE

    service_account = var.SERVICE_ACCOUNT_EMAIL
    oauth_scopes    = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }
}
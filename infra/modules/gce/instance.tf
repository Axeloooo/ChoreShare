# Google Cloud Compute Engine (GCE) instance module

resource "google_compute_instance" "choreshare" {
  name         = var.INSTANCE_NAME
  machine_type = var.MACHINE_TYPE
  zone         = "${var.REGION}-a"

  boot_disk {
    initialize_params {
      image = var.IMAGE_FAMILY
    }
  }

  network_interface {
    network = google_compute_network.choreshare.name

    access_config {
      // Ephemeral public IP
    }
  }

  metadata = {
    ssh-keys = "debian:${file("~/.ssh/gcp_jenkins_key.pub")}"
  }

  metadata_startup_script = file("${path.module}/startup.sh")
}

resource "google_compute_network" "choreshare" {
  name = var.NETWORK_NAME
}

resource "google_compute_firewall" "choreshare" {
  name    = var.FIREWALL_NAME
  network = google_compute_network.choreshare.name

  allow {
    protocol = "tcp"
    ports    = ["80", "8080", "22"]
  }

  source_ranges = ["0.0.0.0/0"]
}
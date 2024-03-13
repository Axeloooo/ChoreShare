# Google Cloud Compute Engine (GCE) instance module

resource "google_compute_instance" "jenkins" {
  name         = var.INSTANCE_NAME
  machine_type = var.MACHINE_TYPE
  zone         = "${var.REGION}-a"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }

  network_interface {
    network = google_compute_network.jenkins.name

    access_config {
      // Ephemeral public IP
    }
  }

  metadata = {
    ssh-keys = "debian:${file("~/.ssh/gcp_jenkins_key.pub")}"
  }

  metadata_startup_script = file("${path.module}/startup.sh")
}

resource "google_compute_network" "jenkins" {
  name = var.NETWORK_NAME
}

resource "google_compute_firewall" "jenkins" {
  name    = var.FIREWALL_NAME
  network = google_compute_network.jenkins.name

  allow {
    protocol = "tcp"
    ports    = ["80", "8080", "22"]
  }

  source_ranges = ["0.0.0.0/0"]
}

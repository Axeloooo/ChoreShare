output "choreshare_vm_name" {
  value = google_compute_instance.choreshare.name
  
  description = "The name of the GCE instance"
}

output "choreshare_vm_external_ip" {
  value       = google_compute_instance.choreshare.network_interface.0.access_config.0.nat_ip
  description = "The external IP address of the ChoreShare VM"
}
